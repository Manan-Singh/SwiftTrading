import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {TwoMovingAverages} from './models/TwoMovingAverages';
import {BollingerBands} from './models/BollingerBands';
import {map} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class StrategyService {
  private baseURL = 'http://localhost:8081/api';
  constructor(private http: HttpClient) { }

  createTwoMovingAverageStrategy(twoMovingAverages: TwoMovingAverages ) {
    console.log('Service creating strategy');
    return this.http.post(this.baseURL + '/strategies/movingaverages', twoMovingAverages);
  }

  createBollingerBandsStrategy(bollingerBands: BollingerBands ) {
    console.log('Service creating strategy');
    return this.http.post(this.baseURL + '/strategies/bollinger', bollingerBands);
  }

  getAllStrategies(){
    console.log('Retrieving all strategies');
    return this.http.get(this.baseURL + '/strategies');
  }

  deleteStrategy(strategy:any){
    console.log('Deleting Strategy');
    return this.http.delete(this.baseURL + '/strategies/' + strategy.id);
  }

  getActiveStrategies(){
    console.log('Getting active strategies');
    return this.http.get(this.baseURL + '/strategies/active');
  }

  getTradesById(strategyId: string){
    console.log('Getting trades by Id');
    return this.http.get(this.baseURL + '/strategies/' + strategyId + '/trades');
  }
}
