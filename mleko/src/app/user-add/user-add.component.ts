import { Component, OnInit } from '@angular/core';
import {MlekoService} from "../service/mleko.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-user-add',
  templateUrl: './user-add.component.html',
  styleUrls: ['./user-add.component.css']
})
export class UserAddComponent implements OnInit {

  constructor(private mlekoService: MlekoService,
              private route: Router) {
  }

  ngOnInit(): void {
  }

  saveResult(username: string, email: string) {
    this.mlekoService.savePlayer({name: username, email: email}).subscribe(()=> {
      this.route.navigate(['last-results']);
    });

  }
}
