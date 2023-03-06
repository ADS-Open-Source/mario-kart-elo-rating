import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ScreenSizeService {

  public isDesktopDevice: boolean = true;

  constructor() {
  }

  public setIfDesktop(deviceScreenSize: number): void {
    this.isDesktopDevice = deviceScreenSize >= 1024;
  }
}
