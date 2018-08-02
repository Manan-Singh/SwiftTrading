import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {TwoMovingAverages} from './models/TwoMovingAverages';
import {BollingerBands} from './models/BollingerBands';

@Injectable({
  providedIn: 'root'
})
export class StrategyService {
  private baseURL = 'http://localhost:8081/api';
  public emptyObj = {};
  constructor(private http: HttpClient) { }

  createTwoMovingAverageStrategy(twoMovingAverages: TwoMovingAverages ) {
    return this.http.post(this.baseURL + '/strategies/movingaverages', twoMovingAverages);
  }

  createBollingerBandsStrategy(bollingerBands: BollingerBands ) {
    return this.http.post(this.baseURL + '/strategies/bollinger', bollingerBands);
  }

  getAllStrategies(){
    return this.http.get(this.baseURL + '/strategies');
  }

  deleteStrategy(strategy:any){
    return this.http.delete(this.baseURL + '/strategies/' + strategy.id);
  }

  getTradesById(strategyId: string){
    return this.http.get(this.baseURL + '/strategies/' + strategyId + '/trades');
  }

  pauseStrategy(strategyId: string){
    return this.http.put(this.baseURL + '/strategies/' + strategyId + '/toggleIsActive', this.emptyObj);
  }
}
