import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { first } from 'rxjs/operators';
import { Router,ActivatedRoute } from '@angular/router'; 

import {AuthenticationService} from './services/authentication.service'


@Component({
    selector: 'app-authorization',
    templateUrl: './authorize.component.html',
    styleUrls: ['./authorize.component.css']
  })

  export class AuthorizationComponent implements OnInit {
    loginForm: FormGroup;
    loading = false;
    submitted = false;
    returnUrl : string;
    constructor(
      private formBuilder: FormBuilder,
      private authenticationService: AuthenticationService,
      private router : Router,
      private route : ActivatedRoute

      
    ) { }

    ngOnInit() {
      this.loginForm = this.formBuilder.group({
          username: ['', Validators.required],
          password: ['', Validators.required]
      });
       // reset login status
       this.authenticationService.logout();

       // get return url from route parameters or default to '/'
       this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';
   }

   // convenience getter for easy access to form fields
   get f() { return this.loginForm.controls; }

   enterPressed() {
       //TODO: Remove code duplication
    this.authenticationService.login(this.f.username.value, this.f.password.value)
    .pipe(first())
    .subscribe(
        data => {
            this.router.navigate(["/dashboard"])
        
        },
        error => {
            //this.error = error;
            this.loading = false;
        });
  }

   onSubmit() {
    var buttonName = document.activeElement.getAttribute("Name");
    this.submitted = true;

    // stop here if form is invalid
    if (this.loginForm.invalid) {
        return;
    }
    console.log(buttonName)
    
    this.loading = true;
    if(buttonName=="Register"){
    this.authenticationService.register(this.f.username.value, this.f.password.value)
        .pipe(first())
        .subscribe(
            data => {
                //this.router.navigate([this.returnUrl]);
                console.log(data);
            },
            error => {
                //this.error = error;
                this.loading = false;
            });
        }
    if(buttonName=="Login"){
        this.authenticationService.login(this.f.username.value, this.f.password.value)
            .pipe(first())
            .subscribe(
                data => {
                    this.router.navigate(["/dashboard"])
                
                },
                error => {
                    //this.error = error;
                    this.loading = false;
                });
            }
    
    }

  }

