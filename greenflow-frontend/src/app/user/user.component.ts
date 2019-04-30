import { Component, OnInit } from '@angular/core';
import { tokenKey } from '@angular/core/src/view';
import { HttpClient } from '@angular/common/http';
import { Globals } from '../globals';
import { StringMap } from '@angular/core/src/render3/jit/compiler_facade_interface';
import { HttpHeaders } from '@angular/common/http';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  private username : string
  private registrationDate : Date
  private isEmailEditing : boolean
  private email : string
  private serverURL : string

  constructor(private http: HttpClient,globals : Globals ) {
    this.serverURL = globals.getBaseUrl()
    
  }


  ngOnInit() {
    this.username=JSON.parse(localStorage.getItem("currentUser")).username
    this.registrationDate=JSON.parse(localStorage.getItem("currentUser")).registrationDate
    this.email = JSON.parse(localStorage.getItem("currentUser")).email
    this.isEmailEditing = false
  }

  emailUpdate(){
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    console.log(this.email)
    const headers = new HttpHeaders()
          .set("Authorization",token)
          .append('Content-Type', 'application/json')
    var storage = JSON.parse(localStorage.getItem('currentUser'))
    if(this.email.length == 0){
      this.emailRemove()
      return
    }
    return this.http.request(storage.email ? 'put' : 'post', this.serverURL+"email",{ body: {email : this.email}, headers, observe : 'response',withCredentials : true})
      .subscribe((res : any)=>{
        
        storage.email=this.email
        localStorage.setItem('currentUser',JSON.stringify(storage))
});
  }

  emailRemove(){
    var token=  JSON.parse(localStorage.getItem("currentUser")).token
    console.log(this.email)
    const headers = new HttpHeaders()
          .set("Authorization",token)
          .append('Content-Type', 'application/json')
    var storage = JSON.parse(localStorage.getItem('currentUser'))
    return this.http.delete( this.serverURL+"email",{  headers, observe : 'response',withCredentials : true})
      .subscribe((res : any)=>{
        this.email=""
        storage.email=null
        localStorage.setItem('currentUser',JSON.stringify(storage))
});

  }
}
