import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TwoMovingAverages} from './models/TwoMovingAverages';
import {BollingerBands} from './models/BollingerBands';

@Injectable({
  providedIn: 'root'
})
export class StrategyService {
  private baseURL = 'http://localhost:8081/api';
  constructor(private http: HttpClient) { }

  createTwoMovingAverageStrategy(twoMovingAverages: TwoMovingAverages ) {
    console.log('Service creating strategy');
    return this.http.post(this.baseURL + '/strategies',twoMovingAverages);
  }

  createBollingerBandsStrategy(bollingerBands: BollingerBands ) {
    console.log('Service creating strategy');
    return this.http.post(this.baseURL + '/strategies',bollingerBands);
  }

  getAllStrategies(){
    console.log('Retrieving all strategies');
    return this.http.get(this.baseURL + '/strategies');
  }
}
