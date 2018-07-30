///<reference path="../../../node_modules/rxjs/internal/Observable.d.ts"/>
import { Component, OnInit } from '@angular/core';
import { Router} from '@angular/router';
import { UserServiceService} from '../user-service.service';
@Component({
  selector: 'app-hello-world',
  templateUrl: './hello-world.component.html',
  styleUrls: ['./hello-world.component.css']
})
export class HelloWorldComponent implements OnInit {

  constructor(private router: Router, private userService: UserServiceService) { }

  ngOnInit() {
    console.log("Hello world prints here: ");
    this.userService.getHelloWorld()
      .subscribe(data=>{console.log(data)});
    console.log("hmm");
  }

}
