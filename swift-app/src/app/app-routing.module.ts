import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {RouterModule, Routes} from '@angular/router';

import {HelloWorldComponent} from './hello-world/hello-world.component';
import {StrategyFormComponent} from './strategy-form/strategy-form.component';
const routes: Routes = [
  {path: 'helloWorld', component: HelloWorldComponent},
  {path: 'strategyForm', component: StrategyFormComponent}
]

@NgModule({
  imports: [
    CommonModule,
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ],
  declarations: []
})
export class AppRoutingModule { }
