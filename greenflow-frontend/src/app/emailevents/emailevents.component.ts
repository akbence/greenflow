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


  constructor(private http: HttpClient,globals : Globals ) {
  
  }


  ngOnInit() {
    this.monthly = false
    this.monthly = false

  }

  test(){
    console.log(this.monthly)
  }
  

 
  
}
