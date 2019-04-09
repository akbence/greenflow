import { Component, OnInit } from '@angular/core';
import { tokenKey } from '@angular/core/src/view';
import { Transaction } from './model/transaction';
import {FormControl} from '@angular/forms';
import {MatCard} from '@angular/material'

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {

  public transaction : Transaction  
  public date : FormControl

  constructor() {
      this.transaction = new Transaction()
      this.transaction.transactionDate=new Date()
      this.date=new FormControl(this.transaction.transactionDate)
   }


  ngOnInit() {
    this.transaction.name = "New Item"
    this.transaction.ammount = 0
    this.transaction.currency = "HUF"
    this.transaction.category = null
    this.transaction.paymentType = "CASH"
    this.transaction.transactionDate = new Date()
  }

}
