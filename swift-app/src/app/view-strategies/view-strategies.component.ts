import {Component, Input, OnInit} from '@angular/core';
import {StrategyService} from '../strategy.service';
import {AlertService} from '../alert.service';

@Component({
  selector: 'app-view-strategies',
  templateUrl: './view-strategies.component.html',
  styleUrls: ['./view-strategies.component.css']
})
export class ViewStrategiesComponent implements OnInit {
  @Input() newStrategyToView: any;
  allStrategies: any;
  filteredStrategies: any;
  temp: any = 'All';
  filterHeaders = [ 'All' , 'Active' , 'Inactive' , 'Two Moving Average' , 'Bollinger Bands' ];
  currentFilter: any;


  constructor(private strategyService: StrategyService, private alertService: AlertService) {
    this.allStrategies = [ ];
    this.filteredStrategies = [ ];
  }

  ngOnInit() {
    this.strategyService.getAllStrategies()
      .subscribe(
      data => {
        this.allStrategies = data;
        this.filteredStrategies = data;
      },
      error => {
        console.log('Error retrieving all strategies');
      }
    );
  }

  deleteStrategy(strategy: any) {
    this.strategyService.deleteStrategy(strategy)
      .subscribe(
        data => {
          const index = this.allStrategies.indexOf(strategy);
          this.allStrategies.splice(index,1 );
          this.alertService.success('Successfully deleted strategy');
        },
        error => {
          this.alertService.error('Error deleting strategy! Strategies can not be deleted if it is active');
          console.log('Error deleting strategy');
        }
      );
  }

  filterStrategies(filter: string){
    this.currentFilter = filter;
    this.filteredStrategies = [ ];
    if(filter === 'Active'){
      for(let strategy of this.allStrategies){
        if(strategy.isActive === true){ this.filteredStrategies.push(strategy);}
      }
    }
    else if(filter === 'Inactive'){
      for(let strategy of this.allStrategies){
        if(strategy.isActive === false) {this.filteredStrategies.push(strategy);}
      }
    }
    else if(filter === 'All'){
      this.filteredStrategies = this.allStrategies;
    }
    else if(filter === 'Two Moving Average'){
      for(let strategy of this.allStrategies){
        if('longTime' in strategy){
          this.filteredStrategies.push(strategy);
        }
      }
    }
    else if(filter === 'Bollinger Bands'){
      for(let strategy of this.allStrategies){
        if('stdDev' in strategy){
          this.filteredStrategies.push(strategy);
        }
      }
    }
    return this.filteredStrategies;
  }

  pauseStrategy(strategy:any){
    this.strategyService.pauseStrategy(strategy.id)
      .subscribe(
        data => {
          this.strategyService.getAllStrategies()
            .subscribe(
              data2 => {
                this.allStrategies = data2;
                this.filteredStrategies = data2;
                this.temp = 'All';
              },
              error => {
                console.log('Error retrieving all strategies');
              }
            );

        },
        error =>{

        }
      )
  }

  addNewStrategy(strategy:any){
    this.allStrategies.push(strategy);
    if(this.currentFilter === 'Active' || this.currentFilter === 'All'){
      this.filteredStrategies.push(strategy);
    }
    else if(this.currentFilter === 'Two Moving Average' && 'longTime' in strategy){
      this.filteredStrategies.push(strategy);
    }
    else if(this.currentFilter === 'Bollinger Bands' && 'stdDev' in strategy){
      this.filteredStrategies.push(strategy);
    }
  }


}
