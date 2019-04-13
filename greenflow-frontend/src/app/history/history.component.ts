import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaderResponse, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { first } from 'rxjs/operators';
import { saveAs } from 'file-saver';
import { query } from '@angular/animations';
import { Globals } from '../globals';


declare interface TableData {
    headerRow: string[];
    dataRows: string[][];
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

  constructor(private http: HttpClient,globals : Globals ) {
    this.serverURL = globals.getBaseUrl()
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
          row=[element.name, element.ammount, element.currency, element.category, element.paymentType, element.date, element.expense.toString()]
          console.log(row)
          transformedRows[iterator++]=row;

        });
        this.tableData1 = {
          headerRow: ['Name', 'Amount', 'Currency', 'Category', 'PaymentType', 'Date', 'isExpense', 'Delete?'],
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