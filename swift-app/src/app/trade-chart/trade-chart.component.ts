import { Component, OnInit } from '@angular/core';
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
        },
        error =>{
          console.log('Error retrieving trades by id in trade chart');
        }
      );
  }


}