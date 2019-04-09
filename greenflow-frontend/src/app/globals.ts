import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';


@Injectable()
export class Globals implements HttpInterceptor{
    public baseUrl: string = 'http://localhost:8080/greenflow/rest/'
    
    constructor() {}
    intercept(req: HttpRequest<any>, next: HttpHandler) {
    // Get the auth token from the service. 
    // Clone the request and replace the original headers with
    // cloned headers, updated with the authorization.
    const corsReq = req.clone({
      headers: req.headers.set("Content-Type","application/json")
      .append("Access-Control-Allow-Origin", "http://127.0.0.1:4200")
      .append('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT')
//         .append('Access-Control-Allow-Headers', 'X-Requested-With,content-type')
      .append('Access-Control-Allow-Headers', 'accept, authorization, content-type, x-requested-with')
      .append('Access-Control-Allow-Credentials', "true")
    });
 
    // send cloned request with header to the next handler.
    return next.handle(corsReq);
  }
    
    public getBaseUrl(){
        return this.baseUrl
    }
}