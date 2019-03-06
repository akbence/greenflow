import { Component, OnInit } from '@angular/core';
import { tokenKey } from '@angular/core/src/view';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  private username : string
  private registrationDate : Date

  constructor() {
    
   }

  ngOnInit() {
    this.username=JSON.parse(localStorage.getItem("currentUser")).username
    this.registrationDate=JSON.parse(localStorage.getItem("currentUser")).registrationDate
  }

}
