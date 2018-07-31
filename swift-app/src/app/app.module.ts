import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {FormsModule} from '@angular/forms';
import { AppComponent } from './app.component';
import { HelloWorldComponent } from './hello-world/hello-world.component';
import { AppRoutingModule } from './/app-routing.module';
import {UserServiceService} from './/user-service.service';
import {HttpClientModule} from '@angular/common/http';
import { StrategyFormComponent } from './strategy-form/strategy-form.component';
import { ViewStrategiesComponent } from './view-strategies/view-strategies.component';
import { AlertComponent } from './alert/alert.component';
import { AlertService} from './alert.service';

@NgModule({
  declarations: [
    AppComponent,
    HelloWorldComponent,
    StrategyFormComponent,
    ViewStrategiesComponent,
    AlertComponent,

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule
  ],
  providers: [UserServiceService, AlertService],
  bootstrap: [AppComponent]
})
export class AppModule { }
