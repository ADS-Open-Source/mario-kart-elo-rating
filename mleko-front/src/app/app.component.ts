import {Component, OnInit} from '@angular/core';
import {ScreenSizeService} from "./services/screen-size-service.service";

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit{

  deviceScreenSize: number = 2048;

  constructor(
    private screenSizeService: ScreenSizeService
  ) {
  }

  ngOnInit(): void {
    this.deviceScreenSize = window.screen.width;
    this.screenSizeService.setIfDesktop(this.deviceScreenSize);
  }
}
