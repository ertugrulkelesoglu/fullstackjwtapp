import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class MessagesService {
  constructor(private http: HttpClient) { }

  getMessages(): Observable<any> {
    const url = "http://localhost:8080/messages";
    return this.http.get( url );
  }

  postLoginInfo(input : any): Observable<any> {
    const url = "http://localhost:8080/login";
    return this.http.post( url ,
       {login : input.login,
         password: input.password})
  }


}
