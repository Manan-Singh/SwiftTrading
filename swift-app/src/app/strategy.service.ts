import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class StrategyService {
  private userUrl = '/api';
  constructor(private http: HttpClient) { }

  createStrategy() {
    console.log('Service creating strategy');
    return this.http.get('http://localhost:8081/api/api');
    //return 'Service Created';
  }
}
