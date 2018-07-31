import { Component, OnInit } from '@angular/core';
import {TwoMovingAverages} from '../models/TwoMovingAverages';
import {Strategy} from '../models/Strategy';
import {BollingerBands} from '../models/BollingerBands';
import {StrategyService} from '../strategy.service';
import {UserServiceService} from '../user-service.service';
import {AlertService} from '../alert.service';

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

  constructor(private strategyService: StrategyService, private userService: UserServiceService, private alertService: AlertService) { }
  ngOnInit() {
  }

  newStrategy() {
    console.log('Submitting a new strategy');
    if(this.strategy.flag === 'Two Moving Averages'){
      this.twoMovingAverages.name = this.strategy.name;
      this.twoMovingAverages.close = this.strategy.close;
      this.twoMovingAverages.ticker = this.strategy.ticker;
      console.log(this.twoMovingAverages);
      this.strategyService.createTwoMovingAverageStrategy(this.twoMovingAverages).subscribe(
        data => {
          console.log('Done!');
          location.reload();
          this.alertService.success('Successfully created a new two moving average strategy!');
        },
        error => {
          console.log('Error creating new strategy');
          this.alertService.error('Error creating new strategy');
        });
    }
    else{
      this.bollingerBands.name = this.strategy.name;
      this.bollingerBands.close= this.strategy.close;
      this.bollingerBands.ticker = this.strategy.ticker;
      console.log(this.bollingerBands);
      this.strategyService.createBollingerBandsStrategy(this.bollingerBands).subscribe(
        data => {
          console.log('Done!');
          console.log(data);
          //location.reload();
          this.alertService.success('Successfully created a new bollinger bands strategy!');
          },
        error => {
          this.alertService.error('Error creating new strategy');
          console.log('Error creating new strategy'); });
    }
    console.log('done');

  }
}
