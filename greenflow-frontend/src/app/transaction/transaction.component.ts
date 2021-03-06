import { Component, OnInit } from '@angular/core';
import { tokenKey } from '@angular/core/src/view';
import { Transaction } from './model/transaction';
import { FormControl } from '@angular/forms';
import { Globals } from '../globals';
import { HttpClient, HttpHeaders } from '@angular/common/http'
import { Resolve } from '@angular/router';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit, Resolve<any> {


  resolve(route: import("@angular/router").ActivatedRouteSnapshot, state: import("@angular/router").RouterStateSnapshot) {
    this.queryAllCategories()
  }

  public transaction: Transaction
  public date: FormControl
  public paymentTypes = ['CASH', 'CARD']
  public serverURL: string
  public categoryList: string[]
  public currencyList: string[]

  constructor(private http: HttpClient, globals: Globals) {
    this.serverURL = globals.getBaseUrl()
    this.transaction = new Transaction()
    this.transaction.date = null
    this.date = new FormControl(new Date())
    this.categoryList = []
    this.currencyList = ["HUF","EUR"]
    this.queryAllCategories()
  }



  ngOnInit() {
    this.transaction.name = "New Item"
    this.transaction.amount = 0
    this.transaction.currency = "HUF"
    this.transaction.paymentType = "CASH"
    console.log(this.categoryList[0])
  }

  addExpense() {
    this.transaction.isExpense = true
    this.addTransaction()
  }

  addIncome() {
    this.transaction.isExpense = false
    this.addTransaction()
  }

  addTransaction() {

    this.transaction.date = this.formatDate((<Date>this.date.value)).toString()
    const req = JSON.stringify(this.transaction)
    var token = JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
      .set("Authorization", token)
      .append('Content-Type', 'application/json')
    return this.http.request<any>('post', this.serverURL + "postTransaction", { headers, body: req, observe: 'response', withCredentials: true })
      .subscribe(
        (data: any) => {
        },
        error => {
          //this.error = error;
          console.log("Error" + error);
        });

  }

  queryAllCategories() {
    var token = JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
      .set("Authorization", token);
    return this.http.get<any>(this.serverURL + "categories", { headers, withCredentials: true })
      .pipe()
      .subscribe(
        data => {
          data.forEach(element => {
            this.categoryList.push(element.name)

          })
          this.transaction.category = this.categoryList[0]
        },
        error => {
          //this.error = error;
          console.log("Error" + error);
        });
  }

  private formatDate(date) {
    var d = new Date(date),
      month = '' + (d.getMonth() + 1),
      day = '' + d.getDate(),
      year = d.getFullYear();

    if (month.length < 2) month = '0' + month;
    if (day.length < 2) day = '0' + day;

    return [year, month, day].join('-');
  }

  selectedFile: File
  base64 : string

  onFileChanged(event) {
    this.selectedFile = event.target.files[0]
    var reader = new FileReader();
    /* reader.readAsDataURL(this.selectedFile);
    var _this = this;
    reader.onload = function () {
      _this.base64 = btoa(<any>reader.result);
      console.log("done") */

    
        reader.onload =this._handleReaderLoaded.bind(this);

        reader.readAsBinaryString(event.target.files[0]);
   
  }

  onUpload() {
    var token = JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
      .set("Authorization", token)
      .append("Content-Type","application/octet-stream")
    return this.http.request<any>("post", this.serverURL + "predict", { body: this.base64, headers, withCredentials: true })
      .pipe()
      .subscribe(
        data => {
          console.log(data)
          this.transaction.amount=data.ammount
          this.transaction.currency=data.currency
          this.transaction.paymentType=data.paymentType
        },
        error => {
          //this.error = error;
          console.log("Error" + error);
        });
  }

  _handleReaderLoaded(readerEvt) {
    var binaryString = readerEvt.target.result;
           this.base64= btoa(binaryString);
   }
}
