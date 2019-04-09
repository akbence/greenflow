import { Component, OnInit } from '@angular/core';
import { tokenKey } from '@angular/core/src/view';
import { Transaction } from './model/transaction';

@Component({
  selector: 'app-transaction',
  templateUrl: './transaction.component.html',
  styleUrls: ['./transaction.component.css']
})
export class TransactionComponent implements OnInit {

  

  public transaction : Transaction
  

  constructor() {
      this.transaction = new Transaction()
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
