import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {map} from "rxjs/operators";
@Injectable({
  providedIn: 'root'
})


export class UserServiceService {
  strategy: string;
  private userUrl = '/api';
  constructor(private http:HttpClient) { }

  public getHelloWorld(){
    console.log("got here!");
    return this.http.get('http://localhost:8081/api',{responseType: 'text'});
    //return this.http.get(this.uerUrl,{responseType: 'text'});
  }

  passStrategy(strategy: any){
    this.strategy = strategy;
  }

  getStrategy(){
    return this.strategy;
  }

}

