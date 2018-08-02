import {Component, OnInit} from '@angular/core';
import {StrategyService} from '../strategy.service';
import {UserServiceService} from '../user-service.service';

@Component({
  selector: 'app-trade-history',
  templateUrl: './trade-history.component.html',
  styleUrls: ['./trade-history.component.css']
})
export class TradeHistoryComponent implements OnInit {
  strategy: any;
  strategyId: string;
  trades: any;
  constructor(private strategyService: StrategyService, private userService: UserServiceService) {
    this.trades = [];
    this.strategy = this.userService.getStrategy();
    this.strategyId = this.strategy.id;
  }

  ngOnInit() {
    console.log('trade history');
    this.strategyService.getTradesById(this.strategyId)
      .subscribe(
        data => {
          this.trades = data;
        },
        error => {
          console.log('Error retrieving trades by Id');
        }
      );
  }

}
