import { Component, OnInit } from '@angular/core';
import { LocationStrategy, PlatformLocation, Location } from '@angular/common';
import { LegendItem, ChartType } from '../lbd/lbd-chart/lbd-chart.component';
import * as Chartist from 'chartist';
import {FormControl} from '@angular/forms';
import {MatDatepicker} from '@angular/material/datepicker';
import * as _moment from 'moment';
// tslint:disable-next-line:no-duplicate-imports
import { Moment} from 'moment'
import {DateAdapter, MAT_DATE_FORMATS, MAT_DATE_LOCALE} from '@angular/material/core';
import {MomentDateAdapter} from '@angular/material-moment-adapter';
import { HttpHeaders, HttpClient } from '@angular/common/http';
import { Globals } from '../globals';

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
  providers: [ {provide: DateAdapter, useClass: MomentDateAdapter, deps: [MAT_DATE_LOCALE]},
               {provide: MAT_DATE_FORMATS, useValue: MY_FORMATS}]
})
export class HomeComponent implements OnInit {

    public pieChartLabels:string[] = ['Chrome', 'Safari', 'Firefox','Internet Explorer','Other'];
    public pieChartData:number[] = [60, 20, 20 , 10,10];
    public pieChartType:string = 'pie';
    public serverURL : string;

    public chartOptions = {
      maintainAspectRatio: false
   };

   
   constructor(private http: HttpClient,globals : Globals ) {
    this.serverURL = globals.getBaseUrl()
   }

  ngOnInit() {
    this.getStats(moment().toDate())
    } 


    getStats(date : Date){
      console.log(date)
      var token=  JSON.parse(localStorage.getItem("currentUser")).token
      const headers = new HttpHeaders()
              .set("Authorization",token)
              .append('Content-Type', 'application/json');
      //const params = new HttpParams().set('name',row);
      return this.http.get(this.serverURL+"statistics/" + date.getFullYear()  +"/" + (date.getMonth()+1),{ headers, observe : 'response'})
      .subscribe((res : any)=>{
        console.log(res.body.labels)
        this.pieChartLabels=res.body.labels
        this.pieChartData=res.body.data
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
