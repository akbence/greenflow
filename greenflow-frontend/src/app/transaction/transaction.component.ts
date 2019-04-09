import { Component, OnInit } from '@angular/core';
import { tokenKey } from '@angular/core/src/view';
import { Transaction } from './model/transaction';
import {FormControl} from '@angular/forms';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {

  public transaction : Transaction  

  date = new FormControl(new Date());
  serializedDate = new FormControl((new Date()).toISOString());

  constructor() {
      this.transaction = new Transaction()
   }


  ngOnInit() {
    document.getElementById('transactionDate').nodeValue = new Date().toDateString()
    
    this.transaction.name = "New Item"
    this.transaction.ammount = 0
    this.transaction.currency = "HUF"
    this.transaction.category = null
    this.transaction.paymentType = "CASH"
    this.transaction.transactionDate = <any> new Date().toLocaleDateString()
    console.log( this.transaction.transactionDate)
  }

}
