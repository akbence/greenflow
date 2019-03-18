import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaderResponse, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { first } from 'rxjs/operators';
import { saveAs } from 'file-saver';
import { query } from '@angular/animations';


declare interface TableData {
    headerRow: string[];
    dataRows: string[][];
}

@Component({
  selector: 'category-tables',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {
    public tableData1: TableData;
    public tableData2: TableData;
    public serverURL : string;

  constructor(private http: HttpClient ) {
    this.serverURL = "api/"
   }

  ngOnInit() {
      this.queryAll();
      this.tableData1 = {
        headerRow: ['Name', 'Delete'],
        dataRows: [[]]
    };      
  }


  queryAll(){
    console.log("start query")
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token);
    return this.http.get<any>(this.serverURL+"categories",{withCredentials: true ,headers},)
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
          row=[element]
          console.log(row)
          transformedRows[iterator++]=row;

        });
        this.tableData1 = {
          headerRow: ['Name', 'Delete'],
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
    return this.http.get<Blob>(this.serverURL+"export",{responseType: 'csv' as 'json', withCredentials: true ,headers},)
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