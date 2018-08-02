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
  @Output() eventClicked = new EventEmitter();
  constructor(private strategyService: StrategyService, private userService: UserServiceService) {
    this.trades = [ ];
    this.strategy = this.userService.getStrategy();
    this.strategyId = this.strategy.id;
  }
  ngOnInit() {
    this.strategyService.getTradesById(this.strategyId)
      .subscribe(
        data => {
          //for loop
          let dates = [];
          let prices = [];
          for(let res in data){
            dates.push(data[res].timeTransacted);
            prices.push(data[res].price);
          }
          for(let res in dates){
            const splitDates = dates[res].split('T');
            dates[res] = splitDates[1];
          }
          this.chart = new Chart('canvas', {
            type: 'line',
            data: {
              labels: dates,
              datasets: [
                {
                  data: prices,
                  borderColor: '#3cba9f',
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
                  display: true
                }],
              }
            }
          });
          this.calculatePnl();
        },
        error =>{
          console.log('Error retrieving trades by id in trade chart');
        }
      );
  }
  refreshTrades(){
    this.strategyService.getTradesById(this.strategyId)
      .subscribe(
        data => {
          //for loop
          let dates = [];
          let prices = [];
          for(let res in data){
            dates.push(data[res].timeTransacted);
            prices.push(data[res].price);
          }
          for(let res in dates){
            const splitDates = dates[res].split('T');
            dates[res] = splitDates[1];
          }
          this.chart = new Chart('canvas', {
            type: 'line',
            data: {
              labels: dates,
              datasets: [
                {
                  data: prices,
                  borderColor: '#3cba9f',
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
                  display: true
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
          console.log(data);
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
