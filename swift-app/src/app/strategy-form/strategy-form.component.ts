import { Component, OnInit } from '@angular/core';
import {TwoMovingAverages} from '../models/TwoMovingAverages';
import {Strategy} from '../models/Strategy';
import {BollingerBands} from '../models/BollingerBands';
import {StrategyService} from '../strategy.service';

@Component({
  selector: 'app-strategy-form',
  templateUrl: './strategy-form.component.html',
  styleUrls: ['./strategy-form.component.css']
})
export class StrategyFormComponent implements OnInit {
  strategy = new Strategy();
  twoMovingAverages = new TwoMovingAverages();
  bollingerBands = new BollingerBands();
  strategies = ['Two Moving Averages', 'Bollinger Bands'];
  ticker = ['AAPL', 'GOOG', 'BRK-A', 'NSC', 'MSFT'];

  constructor(private strategyService: StrategyService) { }
  ngOnInit() {
  }

  newStrategy() {
    console.log('Submitting a new strategy');
    if(this.strategy.flag === 'Two Moving Averages'){
      this.twoMovingAverages.strategy_name = this.strategy.strategy_name;
      this.twoMovingAverages.exit_position = this.strategy.exit_position;
      this.twoMovingAverages.ticker = this.strategy.ticker;
      console.log(this.twoMovingAverages);
      this.strategyService.createTwoMovingAverageStrategy(this.twoMovingAverages).subscribe(
        data => { console.log('Done!'); },
        error => {
          console.log('Error creating new strategy'); });
    }
    else{
      this.bollingerBands.strategy_name = this.strategy.strategy_name;
      this.bollingerBands.exit_position = this.strategy.exit_position;
      this.bollingerBands.ticker = this.strategy.ticker;
      console.log(this.bollingerBands);
      this.strategyService.createBollingerBandsStrategy(this.bollingerBands).subscribe(
        data => { console.log('Done!'); },
        error => {
          console.log('Error creating new strategy'); });
    }
    console.log('done');

  }
}
