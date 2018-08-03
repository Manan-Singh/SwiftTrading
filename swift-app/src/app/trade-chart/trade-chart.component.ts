import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {StrategyService} from '../strategy.service';
import {UserServiceService} from '../user-service.service';
import {Chart} from 'chart.js';


@Component({
  selector: 'app-trade-chart',
  templateUrl: './trade-chart.component.html',
  styleUrls: ['./trade-chart.component.css']
})


export class TradeChartComponent implements OnInit {
  chart = [];
  strategy: any;
  strategyId: string;
  trades: any;
  realPnl = 0;
  potentialPnl = 0;
  longOrPeriod: string;
  shortOrStdDev: string;
  strategyType: string;
  @Output() eventClicked = new EventEmitter();
  constructor(private strategyService: StrategyService, private userService: UserServiceService) {
    this.trades = [ ];
    this.strategy = this.userService.getStrategy();
    this.strategyId = this.strategy.id;
  }
  ngOnInit() {
    this.refreshTrades();
    if('longTime' in this.strategy){
      this.longOrPeriod = this.strategy.longTime;
      this.shortOrStdDev = this.strategy.shortTime;
      this.strategyType = 'Two Moving Average';
    }
    else{
      this.longOrPeriod = this.strategy.period;
      this.shortOrStdDev = this.strategy.stdDev;
      this.strategyType = 'Bollinger Bands';
    }
  }
  refreshTrades(){
    this.strategyService.getTradesById(this.strategyId)
      .subscribe(
        data => {
          let dates = [];
          let profit = [];
          let i = 0;
          let profitTmp = 0;
          for(let res in data){
            i+=1;
          }
          if(i>=2){
            dates = [];
            profitTmp = 0;
            if(data[0].buyTrade === false) {profitTmp += data[0].price * data[0].tradeSize;}
            else{profitTmp -= data[0].price * data[0].tradeSize;}
            for(var res = 1; res < i; res++){
              if(data[res].transactionState !== 'REJECTED'){
                if(data[res].buyTrade === false)
                {
                  profit.push(profitTmp + (data[res].price * data[res].tradeSize));
                  profitTmp = profitTmp + (data[res].price * data[res].tradeSize);
                  dates.push(data[res].timeTransacted);
                }
                else if(data[res].buyTrade === true){
                  profit.push(profitTmp - (data[res].price * data[res].tradeSize));
                  profitTmp = profitTmp - (data[res].price * data[res].tradeSize);
                  dates.push(data[res].timeTransacted);
                }
              }
            }
          }
          for(let res in dates){
            const splitDates = dates[res].split('T');
            dates[res] = splitDates[1];
          }

          let chartColor: string;
          if(profitTmp >= 0){
            chartColor = "#3cba68";

          }
          else{
            chartColor = "#ba3c46"
          }
          this.chart = new Chart('canvas', {
            type: 'line',
            data: {
              labels: dates,
              datasets: [
                {
                  data: profit,
                  borderColor: chartColor,
                  fill: false
                }
              ]
            },
            options: {
              legend: {
                display: false
              },
              scales: {
                xAxes: [{
                  display: true
                }],
                yAxes: [{
                  display: true,
                  ticks:{
                    callback: function(value, index, values){
                      return '$' + value;
                    }
                  }
                }],
              }
            }
          });
          this.calculatePnl();
          this.eventClicked.emit(null);
        },
        error =>{
          console.log('Error retrieving trades by id in trade chart');
        }
      );
  }

  calculatePnl(){
    this.strategyService.getTradesById(this.strategyId)
      .subscribe(
        data =>{
          this.realPnl = 0;
          this.potentialPnl = 0;
          for(let res in data){
            if(data[res].buyTrade === false){this.potentialPnl += (Number(data[res].tradeSize) * Number(data[res].price));}
            else{this.potentialPnl -= (Number(data[res].tradeSize) * Number(data[res].price));}
            if(data[res].transactionState !== 'REJECTED'){
             if(data[res].buyTrade === false){this.realPnl += (Number(data[res].tradeSize) * Number(data[res].price));}
             else{this.realPnl -= (Number(data[res].tradeSize) * Number(data[res].price));}
           }
          }
          this.potentialPnl = Number(this.potentialPnl.toFixed(2));
          this.realPnl = Number(this.realPnl.toFixed(2));
        },
        error =>{
          console.log('Error calculating pnl');
        }
      )

  }

}
