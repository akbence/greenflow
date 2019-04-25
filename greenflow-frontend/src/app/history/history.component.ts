import { Component, OnInit, Inject } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaderResponse, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { first } from 'rxjs/operators';
import { saveAs } from 'file-saver';
import { query } from '@angular/animations';
import { Globals } from '../globals';
import { MAT_DIALOG_DATA,MatDialogRef,MatDialog } from '@angular/material';
import { Transaction } from '../transaction/model/transaction';
import { FormGroup, FormBuilder } from '@angular/forms';


declare interface TableData {
    headerRow: string[];
    dataRows: string[][];
}

export class TransactionModify {
  name: string;
  amount : number
  currency : string
  category : string
  paymentType : string
  date : string
  isExpense : boolean
  id : number
}

@Component({
  selector: 'history-tables',
  templateUrl: './history.component.html',
  styleUrls: ['./history.component.css']
})
export class HistoryComponent implements OnInit {
    public tableData1: TableData;
    public tableData2: TableData;
    public serverURL : string;

  constructor(private http: HttpClient,globals : Globals, public dialog: MatDialog ) {
    this.serverURL = globals.getBaseUrl()
   }

   updateRow(row): void {
    console.log ( row)
    var transaction = new TransactionModify()
    var index=0;
    transaction.name= row[index++]
    transaction.amount=row[index++]
    transaction.currency=row[index++]
    transaction.category =row[index++]
    transaction.paymentType = row[index++]
    transaction.date =row[index++]
    transaction.isExpense = row[index++]
    transaction.id = row[index++]
    const dialogRef = this.dialog.open(ModfiyTransactionDialog, {
      width: '600px',
      data: transaction
    });

    dialogRef.afterClosed().subscribe(result => {
      this.queryAll()
    });
  }


  ngOnInit() {
      this.queryAll();
      this.tableData1 = {
        headerRow: ['Name', 'Amount', 'Currency', 'Category', 'PaymentType', 'Date', 'isExpense'],
        dataRows: [[]]
    };      
  }


  deleteRow(row){
    console.log(row)
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    //const params = new HttpParams().set('name',row);
    return this.http.delete(this.serverURL+"transactions/" + row[7],{ headers, observe : 'response',withCredentials : true})
    .subscribe((res : any)=>{
      this.queryAll();
  });
  }

  queryAll(){
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token);
    return this.http.get<any>(this.serverURL+"transactions",{headers,withCredentials:true})
    .pipe(res =>{
      return res
    }).pipe()
    .subscribe(
      data =>{
        var iterator = 0
        var transformedRows = []
        data.forEach(element => {
          var row : string[]
          row=[element.name, element.ammount, element.currency, element.category, element.paymentType, element.date, element.expense.toString(),element.id]
          console.log(row)
          transformedRows[iterator++]=row;

        });
        this.tableData1 = {
          headerRow: ['Name', 'Amount', 'Currency', 'Category', 'PaymentType', 'Date', 'isExpense', 'Update', 'Delete'],
          dataRows: transformedRows
      };   

       
        console.log(transformedRows)
        return transformedRows;
      },
      error => {
          //this.error = error;
          console.log("Error"+error);
      });
  }
  


  exportAll(){
    console.log("start export")
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token);
    console.log("clicked")
    return this.http.get<Blob>(this.serverURL+"export",{responseType: 'csv' as 'json' ,headers,withCredentials : true},)
    .pipe(res =>{
      return res
    }).pipe()
    .subscribe(
      data =>{
        console.log(data)
        var blob = new Blob([data], { type: "text/csv" } );
        saveAs(blob, "fileName.csv");
      },
      error => {
          //this.error = error;
          console.log("Error"+error);
      });
  }
}

///DIALOG COMPONENT///
@Component({
  selector: 'modify-transaction-dialog',
  templateUrl: './modify.transaction.dialog.html',
})
export class ModfiyTransactionDialog {

  transaction : TransactionModify
  form: FormGroup
  serverURL : string
  categoryPool : string []
  selected : number

  constructor(private http: HttpClient,globals : Globals,
    private fb: FormBuilder,
    public dialogRef: MatDialogRef<ModfiyTransactionDialog>,
    @Inject(MAT_DIALOG_DATA) public data: TransactionModify) {
      this.transaction = data
      this.serverURL = globals.getBaseUrl()
      
    }

  onNoClick(): void {
    this.dialogRef.close();
  }
  save() {
    console.log(this.form.value)
    this.updateRow(this.form.value,this.transaction.id)
    this.dialogRef.close(this.form.value);
}

  close() {
      this.dialogRef.close();
}

  ngOnInit() {
    this.selected=-1
    this.queryCategories()
    this.form = this.fb.group({
      name : [this.transaction.name],
      amount : [this.transaction.amount],
      currency : [this.transaction.currency],
      category : [this.transaction.category],
      paymentType : [this.transaction.paymentType],
      date : [this.transaction.date],
      isExpense : [this.transaction.isExpense]
    });
    console.log(this.transaction)
}
 
queryCategories(){
  var token=  JSON.parse(localStorage.getItem("currentUser")).token
  const headers = new HttpHeaders()
          .set("Authorization",token);
  return this.http.get<any>(this.serverURL+"categories",{headers,withCredentials : true})
  .pipe()
  .subscribe(
    data =>{
      var rows = []
      data.forEach(element => {
        rows.push(element.name)
      });
      this.categoryPool=rows
    },
    error => {
        //this.error = error;
        console.log("Error"+error);
    });
}

updateRow(transaction : TransactionModify, id : number){
  var token=  JSON.parse(localStorage.getItem("currentUser")).token
  const headers = new HttpHeaders()
          .set("Authorization",token)
          .append('Content-Type', 'application/json');
  return this.http.put(this.serverURL+"transactions/" + id,transaction,{ headers, observe : 'response',withCredentials : true})
  .subscribe((res : any)=>{
});
}

}