import {Component, Input, OnInit} from '@angular/core';
import {StrategyService} from '../strategy.service';
import {AlertService} from '../alert.service';
import {Alert, AlertType} from '../models/alert';

@Component({
  selector: 'app-view-strategies',
  templateUrl: './view-strategies.component.html',
  styleUrls: ['./view-strategies.component.css']
})
export class ViewStrategiesComponent implements OnInit {

  @Input() id: string;

  allStrategies: any;
  filteredStrategies: any;
  temp: any = 'All';
  filterHeaders = [ 'All' , 'Active' , 'Inactive' , 'Two Moving Average' , 'Bollinger Bands' ];
  chart = [];
  testData: any;

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
    this.testData = {"message":"","cod":"200","city_id":2643743,"calctime":0.0875,"cnt":3,"list":[{"main":{"temp":279.946,"temp_min":279.946,"temp_max":279.946,"pressure":1016.76,"sea_level":1024.45,"grnd_level":1016.76,"humidity":100},"wind":{"speed":4.59,"deg":163.001},"clouds":{"all":92},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"rain":{"3h":2.69},"dt":1485717216},{"main":{"temp":282.597,"temp_min":282.597,"temp_max":282.597,"pressure":1012.12,"sea_level":1019.71,"grnd_level":1012.12,"humidity":98},"wind":{"speed":4.04,"deg":226},"clouds":{"all":92},"weather":[{"id":500,"main":"Rain","description":"light rain","icon":"10n"}],"rain":{"3h":0.405},"dt":1485745061},{"main":{"temp":279.38,"pressure":1011,"humidity":93,"temp_min":278.15,"temp_max":280.15},"wind":{"speed":2.6,"deg":30},"clouds":{"all":90},"weather":[{"id":701,"main":"Mist","description":"mist","icon":"50d"},{"id":741,"main":"Fog","description":"fog","icon":"50d"}],"dt":1485768552}]};
    let temp_max = this.testData['list'].map(res => res.main.temp_max);
    let temp_min = this.testData['list'].map(res => res.main.temp_min);
    let alldates = this.testData['list'].map(res => res.dt)

    let weatherDates = []
    alldates.forEach((res) => {
      let jsdate = new Date(res * 1000)
      weatherDates.push(jsdate.toLocaleTimeString('en', { year: 'numeric', month: 'short', day: 'numeric' }))
    })
  }

  deleteStrategy(strategy: any) {
    console.log(strategy);
    this.strategyService.deleteStrategy(strategy)
      .subscribe(
        data => {
          const index = this.allStrategies.indexOf(strategy);
          this.allStrategies.splice(index,1 );
          this.alertService.success('Successfully deleted strategy');
        },
        error => {
          this.alertService.error('Error deleting strategy!')
          console.log('Error deleting strategy');
        }
      );
  }

  filterStrategies(filter: string){
    console.log(filter);
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
    console.log("Pausing strategy");
  }

}
