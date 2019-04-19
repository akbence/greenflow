import { Component, OnInit, Inject } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaderResponse, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { first } from 'rxjs/operators';
import { saveAs } from 'file-saver';
import { query } from '@angular/animations';
import { Globals } from '../globals';
import { MAT_DIALOG_DATA,MatDialogRef,MatDialog } from '@angular/material';
import { Transaction } from '../transaction/model/transaction';


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
    transaction.name= row[0]
    transaction.amount=row[1]
    transaction.currency=row[2]
    transaction.category =row[3]
    transaction.paymentType = row[4]
    transaction.date =row[5]
    transaction.isExpense = row[6]
    transaction.id = row[7]
    console.log(transaction)
    const dialogRef = this.dialog.open(ModfiyTransactionDialog, {
      //height: '400px',
      width: '600px',
      data: transaction
    });

    dialogRef.afterClosed().subscribe(result => {
      console.log('The dialog was closed');
      //this.animal = result;
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
    return this.http.delete(this.serverURL+"transactions/" + row[7],{ headers, observe : 'response'})
    .subscribe((res : any)=>{
      this.queryAll();
  });
  }


  queryAll(){
    console.log("start query")
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token);
    return this.http.get<any>(this.serverURL+"transactions",{headers},)
    .pipe(res =>{
      return res
    }).pipe()
    .subscribe(
      data =>{
        console.log(data)
        
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
    return this.http.get<Blob>(this.serverURL+"export",{responseType: 'csv' as 'json' ,headers},)
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

@Component({
  selector: 'modify-transaction-dialog',
  templateUrl: './modify.transaction.dialog.html',
})
export class ModfiyTransactionDialog {

  constructor(
    public dialogRef: MatDialogRef<ModfiyTransactionDialog>,
    @Inject(MAT_DIALOG_DATA) public data: TransactionModify) {
      console.log("THIS IS DIALOG CTOR: " + data)
    }

  onNoClick(): void {
    this.dialogRef.close();
  }

}