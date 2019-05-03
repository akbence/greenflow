import { Component, OnInit } from '@angular/core';
import { LocationStrategy, PlatformLocation, Location } from '@angular/common';
import { LegendItem, ChartType } from '../lbd/lbd-chart/lbd-chart.component';
import * as Chartist from 'chartist';
import { FormControl } from '@angular/forms';
import { MatDatepicker } from '@angular/material/datepicker';
import * as _moment from 'moment';
// tslint:disable-next-line:no-duplicate-imports
import { Moment } from 'moment'
import { DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE } from '@angular/material/core';
import { MomentDateAdapter } from '@angular/material-moment-adapter';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Globals } from '../globals';
import { forEach } from '@angular/router/src/utils/collection';
import { Balance } from './model/Balance';

const moment = _moment;

export const MY_FORMATS = {
  parse: {
    dateInput: 'MM/YYYY',
  },
  display: {
    dateInput: 'MM/YYYY',
    monthYearLabel: 'MMM YYYY',
    dateA11yLabel: 'LL',
    monthYearA11yLabel: 'MMMM YYYY',
  },
};


@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
  providers: [{ provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE] },
  { provide: MAT_DATE_FORMATS, useValue: MY_FORMATS }]
})
export class HomeComponent implements OnInit {

  public pieIncomeChartLabels: string[] = new Array()
  public pieIncomeChartData: number[] = []
  public pieExpenseChartLabels: string[] = []
  public pieExpenseChartData: number[] = []
  public pieIncomeDatas: any[]
  public pieExpenseDatas: any[]
  public pieChartType: string = 'pie';
  public serverURL: string;

  public selectedBalance
  public balance : Balance


  public barChartLabels: string[] 
  public barChartType: string  = 'bar'
  public barChartLegend = true
  public barChartData = []
  public selectedMonths : number
  public selectedCurrency : string
  public monthsPool : string




  pieDataReady1: Promise<boolean>
  pieDataReady2: Promise<boolean>
  barDataReady: Promise<boolean>

  public chartOptions = {
    maintainAspectRatio: false
  };


  constructor(private http: HttpClient, globals: Globals) {
    this.serverURL = globals.getBaseUrl()
  }

  ngOnInit() {
    this.monthsPool = (Array.apply(null, {length: 12}).map(Number.call, Number)).map(function(val){return ++val;})
    this.selectedMonths= 3
    this.selectedCurrency= 'HUF'
    this.selectedBalance= 'TOTAL'
    this.balance= new Balance()
    this.getExchangeRateEurToHuf()
    this.getStats(moment().toDate())
    this.getBalance()
    this.getBarStats()
  }

  getStats(date: Date) {
    this.getIncomeStats(date)
    this.getExpenseStats(date)
  }

  getExchangeRateEurToHuf(){
    return this.http.get("https://api.exchangeratesapi.io/latest?symbols=HUF", { observe: 'response'})
      .subscribe((res: any) => {
        this.balance.exchangeRateEurToHuf=res.body.rates.HUF
        this.balance.convertEurToHuf()
      });
  }

  getBalance(){
    var token = JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
      .set("Authorization", token)
      .append('Content-Type', 'application/json');
    return this.http.get(this.serverURL + "statistics/balance", { headers, observe: 'response', withCredentials: true })
      .subscribe((res: any) => {
        this.balance.set(res.body)
        this.balance.convertEurToHuf()
      });
  }



  selectedBalanceChanged(value){
    if(value=="CASH"){
      this.balance.showCash()
    }
    if(value=="CARD"){
      this.balance.showCard()
    }
    if(value=="TOTAL"){
      this.balance.showTotal()
    }
  }

  getIncomeStats(date: Date) {
    var token = JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
      .set("Authorization", token)
      .append('Content-Type', 'application/json');
    //const params = new HttpParams().set('name',row);
    return this.http.get(this.serverURL + "statistics/pie/income/" + date.getFullYear() + "/" + (date.getMonth() + 1), { headers, observe: 'response', withCredentials: true })
      .subscribe((res: any) => {
        this.pieIncomeDatas = res.body
        this.pieDataReady1 = Promise.resolve(true)
      });
  }

  getExpenseStats(date: Date) {
    var token = JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
      .set("Authorization", token)
      .append('Content-Type', 'application/json');
    //const params = new HttpParams().set('name',row);
    return this.http.get(this.serverURL + "statistics/pie/expense/" + date.getFullYear() + "/" + (date.getMonth() + 1), { headers, observe: 'response', withCredentials: true })
      .subscribe((res: any) => {
        this.pieExpenseDatas = res.body
        this.pieDataReady2 = Promise.resolve(true)

      });
  }

  getBarStats() {
    var token = JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
      .set("Authorization", token)
      .append('Content-Type', 'application/json');

    return this.http.get(this.serverURL + "statistics/bar",{params: {months : this.selectedMonths.toString(), currency : this.selectedCurrency}, headers, observe: 'response', withCredentials: true })
      .subscribe((res: any) => {
        console.log(res.body)
        this.barChartLabels=res.body.labels
        this.barChartData = [
          { data: res.body.expenseData, label: 'Expense' },
          { data: res.body.incomeData, label: 'Income' }
        ];
        this.barDataReady = Promise.resolve(true)
        
      });
  }


  date = new FormControl(moment());

  chosenYearHandler(normalizedYear: Moment) {
    const ctrlValue = this.date.value;
    ctrlValue.year(normalizedYear.year());
    this.date.setValue(ctrlValue);
  }

  chosenMonthHandler(normalizedMonth: Moment, datepicker: MatDatepicker<Moment>) {
    const ctrlValue = this.date.value;
    ctrlValue.month(normalizedMonth.month());
    this.date.setValue(ctrlValue);
    datepicker.close();
    this.getStats(ctrlValue.toDate())
  }
}
