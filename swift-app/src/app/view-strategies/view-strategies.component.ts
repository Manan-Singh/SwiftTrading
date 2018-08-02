import {Component, OnInit} from '@angular/core';
import {StrategyService} from '../strategy.service';
import {AlertService} from '../alert.service';

@Component({
  selector: 'app-view-strategies',
  templateUrl: './view-strategies.component.html',
  styleUrls: ['./view-strategies.component.css']
})
export class ViewStrategiesComponent implements OnInit {

  allStrategies: any;
  filteredStrategies: any;
  temp: any = 'All';
  filterHeaders = [ 'All' , 'Active' , 'Inactive' , 'Two Moving Average' , 'Bollinger Bands' ];


  constructor(private strategyService: StrategyService, private alertService: AlertService) {
    this.allStrategies = [ ];
    this.filteredStrategies = [ ];
  }

  ngOnInit() {
    this.strategyService.getAllStrategies()
      .subscribe(
      data => {
        console.log(data);
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
    this.filteredStrategies = [ ];
    if(filter === 'Active'){
      for(let strategy of this.allStrategies){
        if(strategy.isActive === true){ console.log(strategy); this.filteredStrategies.push(strategy);}
      }
      //this.filteredStrategies = this.strategyService.getActiveStrategies();
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
        console.log(strategy.stdDev)
        if('stdDev' in strategy){
          this.filteredStrategies.push(strategy);
        }
      }
    }
    return this.filteredStrategies;
  }

  strategyDetails(strategy:any){
    console.log('Retrieving strategy details');
  }

  pauseStrategy(strategy:any){
    console.log('Pausing strategy');
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


}
