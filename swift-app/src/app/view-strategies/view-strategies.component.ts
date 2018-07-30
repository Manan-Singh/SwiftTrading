import { Component, OnInit } from '@angular/core';
import {StrategyService} from '../strategy.service';
import {Router} from '@angular/router';
import {Strategy} from '../models/Strategy';
import {map} from 'rxjs/operators';
import {Observable} from 'rxjs';

@Component({
  selector: 'app-view-strategies',
  templateUrl: './view-strategies.component.html',
  styleUrls: ['./view-strategies.component.css']
})
export class ViewStrategiesComponent implements OnInit {

  allStrategies: Strategy[];
  constructor(private strategyService: StrategyService) {
    this.allStrategies = [ ];
  }

  ngOnInit() :Observable<Strategy> {
    this.strategyService.getAllStrategies()
      .map(res => res)
      .subscribe(
      data => {
        console.log(data);
      },
      error => {
        console.log('Error retrieving all strategies');
      }
    );
  }

}
