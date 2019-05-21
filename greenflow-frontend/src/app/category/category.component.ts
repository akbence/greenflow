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
}
 class TableRow {
  data : string
  id : number

  constructor(a,b){
    this.data=a;
    this.id=b;
  }
} 

@Component({
  selector: 'category-tables',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {
    public tableData1: TableData
    public tableData2: TableData
    public serverURL : string
    public addNew: string

  constructor(private http: HttpClient, globals : Globals) {
    this.serverURL = globals.getBaseUrl()
   }

  ngOnInit() {
      this.queryAll();
      this.tableData1 = {
        headerRow: ['Name', 'Update','Delete'],
        dataRows: [],
    };      
  }


  queryAll(){
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token);
    return this.http.get<any>(this.serverURL+"categories",{headers, withCredentials : true})
    .pipe()
    .subscribe(
      data =>{

        var iterator = 0
        var transformedRows = []
        data.forEach(element => {
          var row = new TableRow(element.name,element.id)
          transformedRows.push(row)
        });
        this.tableData1 = {
          headerRow: ['Name', 'Update','Delete'],
          dataRows: transformedRows,
      };   
        return transformedRows;
      },
      error => {
          //this.error = error;
          console.log("Error"+error);
      });
  }
  
  addRow(){
    console.log(this.addNew)
    const req=JSON.stringify({name : this.addNew});
    console.log("start query post with " + req)
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    //const params = new HttpParams().set('name',row);
    return this.http.request<any>('post',this.serverURL+"postCategory",{headers, body : req, observe : 'response',withCredentials : true})
    .subscribe((res : any)=>{
      this.queryAll();
  });
   }



  updateRow(row){
    console.log(row)
    const req=JSON.stringify({name : row.data});
    console.log("start query post with " + req)
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    //const params = new HttpParams().set('name',row);
    return this.http.request<any>('put',this.serverURL+"categories/"+ row.id,{headers, body : req, observe : 'response',withCredentials : true})
    .subscribe((res : any)=>{
      this.queryAll();
  });
   }

  deleteRow(row){
    console.log("start query delete with " + row)
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
            .set("Authorization",token)
            .append('Content-Type', 'application/json');
    //const params = new HttpParams().set('name',row);
    return this.http.request<any>('delete',this.serverURL+"categories/" + row.id,{ headers, observe : 'response',withCredentials : true})
    .subscribe((res : any)=>{
      this.queryAll();
  });
    }
}