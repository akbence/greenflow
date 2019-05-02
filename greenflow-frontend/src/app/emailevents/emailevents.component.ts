import { Component, OnInit } from '@angular/core';
import { tokenKey } from '@angular/core/src/view';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../globals';
import { StringMap } from '@angular/core/src/render3/jit/compiler_facade_interface';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-emailevents',
  templateUrl: './emailevents.component.html'
})
export class EmaileventsComponent implements OnInit {

  private monthly : boolean
  private warning : boolean
  private serverURL : string
  

  constructor(private http: HttpClient,globals : Globals ) {
    this.serverURL = globals.getBaseUrl()
  }


  ngOnInit() {
    this.monthly = false
    this.warning = false
  }

  updateEvents(){
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    const headers = new HttpHeaders()
          .set("Authorization",token)
          .append('Content-Type', 'application/json')
    var storage = JSON.parse(localStorage.getItem('currentUser'))
    return this.http.put(this.serverURL+"events",{monthlyreport : this.monthly, warningreport: this.warning },{  headers, observe : 'response',withCredentials : true})
      .subscribe((res : any)=>{
});
  }
  

 
  
}
