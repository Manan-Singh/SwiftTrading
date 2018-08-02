import {Component, Output, OnInit, EventEmitter} from '@angular/core';
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
  flag: any = 'Strategy';
  @Output() addEvent = new EventEmitter<any>();

  constructor(private strategyService: StrategyService, private userService: UserServiceService, private alertService: AlertService) { }
  ngOnInit() {
  }

  newStrategy() {
    if(this.flag === 'Two Moving Averages'){
      this.twoMovingAverages.name = this.strategy.name;
      this.twoMovingAverages.close = this.strategy.close;
      this.twoMovingAverages.ticker = this.strategy.ticker;
      this.twoMovingAverages.entrySize = this.strategy.entrySize;
      this.strategyService.createTwoMovingAverageStrategy(this.twoMovingAverages).subscribe(
        data => {
          this.addEvent.emit(data);
          this.alertService.success('Successfully created a new two moving average strategy!');
        },
        error => {
          console.log('Error creating new strategy');
          this.alertService.error('Error creating new strategy');
        });
    }
    else if(this.flag === 'Bollinger Bands'){
      this.bollingerBands.name = this.strategy.name;
      this.bollingerBands.close= this.strategy.close;
      this.bollingerBands.ticker = this.strategy.ticker;
      this.bollingerBands.entrySize = this.strategy.entrySize;
      this.strategyService.createBollingerBandsStrategy(this.bollingerBands).subscribe(
        data => {
          this.addEvent.emit(data);
          this.alertService.success('Successfully created a new bollinger bands strategy!');
          },
        error => {
          this.alertService.error('Error creating new strategy');
          console.log('Error creating new strategy'); });
    }
  }
}
