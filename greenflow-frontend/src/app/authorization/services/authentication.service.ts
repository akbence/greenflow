import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpHeaderResponse, HttpHeaders, } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import {CookieService} from 'ngx-cookie-service'


import { User } from '../model/user';
import { Globals } from 'src/app/globals';

@Injectable({ providedIn: 'root' })
export class AuthenticationService {
    private currentUserSubject: BehaviorSubject<User>;
    public currentUser: Observable<User>;
    public globals: Globals;
    public serverURL :string;

    constructor(private http: HttpClient,private cookie:CookieService, globals: Globals) {
        this.currentUserSubject = new BehaviorSubject<User>(JSON.parse(localStorage.getItem('currentUser')));
        this.currentUser = this.currentUserSubject.asObservable();
        this.serverURL = globals.getBaseUrl()
    }

    public get currentUserValue(): User {
        return this.currentUserSubject.value;
    }

    register(username: string, password: string){
        const data ={
            username : username,
            password : password
        }

        return this.http.post( this.serverURL  + "register",{ username, password});
    }

    login(username: string, password: string) {
        
        return this.http.post<any>(this.serverURL + "login", { username, password},{withCredentials : true})
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