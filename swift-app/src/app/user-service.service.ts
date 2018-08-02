import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})


export class UserServiceService {
  strategy: string;

  constructor(private http:HttpClient) { }

  passStrategy(strategy: any){
    this.strategy = strategy;
  }

  getStrategy(){
    return this.strategy;
  }

}

