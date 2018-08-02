import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Swift';
  newStrategy: any;
  addingEvent(strategy: any){
    this.newStrategy = strategy;
  }
}
