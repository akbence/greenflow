import { Component, OnInit } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaderResponse, HttpHeaders } from '@angular/common/http';
import { map } from 'rxjs/operators';
import { first } from 'rxjs/operators';
import { saveAs } from 'file-saver';
import { query } from '@angular/animations';
import { Globals } from '../globals';





class TableData {
    headerRow: string[];
    dataRows: TableRow[];
    oldDataRows: string[][];
}
 class TableRow {
  data : string
  oldData: string

  constructor(a,b){
    this.data=a;
    this.oldData=b;
  }
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

  constructor(private http: HttpClient, globals : Globals) {
    this.serverURL = globals.getBaseUrl()
   }

  ngOnInit() {
      this.queryAll();
      this.tableData1 = {
        headerRow: ['Name', 'Update','Delete'],
        dataRows: [],
        oldDataRows: [[]]
    };      
  }


  queryAll(){
    console.log("start query")
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token);
    return this.http.get<any>(this.serverURL+"categories",{headers})
    .pipe()
    .subscribe(
      data =>{
        console.log(data)        
        var iterator = 0
        var transformedRows = []
        data.forEach(element => {
//          var row : string[]
          var row = new TableRow(element,element)
          //row.data=element
          //row.oldData=element
          console.log(row)
          transformedRows.push(row)
//          transformedRows[iterator++]=row;
        });
        this.tableData1 = {
          headerRow: ['Name', 'Update','Delete'],
          dataRows: transformedRows,
          oldDataRows: transformedRows
      };   
        console.log(transformedRows)
        return transformedRows;
      },
      error => {
          //this.error = error;
          console.log("Error"+error);
      });
  }
  
  addRow(name){
    console.log(name)
    const req=JSON.stringify({name : name});
    console.log("start query post with " + req)
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    //const params = new HttpParams().set('name',row);
    return this.http.request<any>('post',this.serverURL+"postCategory",{headers, body : req, observe : 'response'})
    .subscribe((res : any)=>{
      this.queryAll();
  });
   }



  updateRow(row){
    console.log(row)
    const req=JSON.stringify({name : row.data, oldValue : row.oldData});
    console.log("start query post with " + req)
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    //const params = new HttpParams().set('name',row);
    return this.http.request<any>('put',this.serverURL+"modfiyCategory",{headers, body : req, observe : 'response'})
    .subscribe((res : any)=>{
      this.queryAll();
  });
   }

  deleteRow(row){
    const req=JSON.stringify({name : row.data});
    console.log("start query delete with " + req)
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    //const params = new HttpParams().set('name',row);
    return this.http.request<any>('delete',this.serverURL+"deleteCategory",{ headers, body : req, observe : 'response'})
    .subscribe((res : any)=>{
      this.queryAll();
  });
    }
}