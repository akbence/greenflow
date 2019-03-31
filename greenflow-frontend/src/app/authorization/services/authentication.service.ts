import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaderResponse, HttpHeaders, } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {CookieService} from 'ngx-cookie-service'

import { User } from '../model/user';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    private currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>;

    public serverURL : string;

    constructor(private http: HttpClient,private cookie:CookieService) {
        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
        //this.serverURL= "http://localhost:8080/greenflow/rest/";
        this.serverURL = "api/";
    }

    public get currentUserValue(): User {
        return this.currentUserSubject.value;
    }

    register(username: string, password: string){
        let headers = new HttpHeaders().set("Content-Type","application/json")
         .append("Access-Control-Allow-Origin", "http://127.0.0.1:8080")
         .append('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE')
//         .append('Access-Control-Allow-Headers', 'X-Requested-With,content-type')
         .append('Access-Control-Allow-Headers', 'accept, authorization, content-type, x-requested-with')
         .append('Access-Control-Allow-Credentials', "true")
        
        const data ={
            username : username,
            password : password
        }
        console.log(headers.get("Access-Control-Allow-Origin"))
        return this.http.post(/* this.serverURL */ "http://localhost:8080/" + "register",{headers,data});
    }

    login(username: string, password: string) {
        return this.http.post<any>(/* this.serverURL */"http://localhost:8080/" + "login", { username, password },{withCredentials: true})
            .pipe(map(response => {
                // login successful if there's a jwt token in the response
                if (response && response.token) {
                    // store user details and jwt token in local storage to keep user logged in between page refreshes
                   console.log(localStorage.setItem('currentUser', JSON.stringify(response)))
                   this.currentUserSubject.next(response);
                }
                    console.log(response)
                return response;
            }));
    }

    

    logout() {
        // remove user from local storage to log user out
        localStorage.removeItem('currentUser');
        this.currentUserSubject.next(null);
    }
}