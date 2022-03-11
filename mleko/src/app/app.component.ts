import { Component } from '@angular/core';
import {
  Router,
  RoutesRecognized
} from "@angular/router";
import {ReplaySubject} from "rxjs";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'mleko';
  static secret = new ReplaySubject<string>(1);
  // @ts-ignore
  secret: string;

  constructor(
    private router: Router
  ) {
    this.router.events.subscribe(event => {
      if (event instanceof RoutesRecognized) {
        if (event.state.root.queryParams['secret']) {
          this.secret = event.state.root.queryParams['secret'];
          AppComponent.secret.next(event.state.root.queryParams['secret']);
        }
      }
    })
  }

  navigate(url: string) {
    this.router.navigate([url], { queryParams: { secret: this.secret } })
  }
}
