import {Injectable, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";

@Injectable({
  providedIn: 'root'
})
export class ScreenSizeService implements OnInit{

  private _isDesktopDevice: boolean = true;

  constructor(
    private responsive: BreakpointObserver,
  ) {
  }

  public get isDesktop(): boolean {
    return this._isDesktopDevice;
  }

  public set isDesktop(isDesktop: boolean) {
    this._isDesktopDevice = isDesktop;
  }

  ngOnInit(): void {

    // TODO why is this not working
    this.responsive.observe([Breakpoints.Handset, Breakpoints.Tablet]).subscribe(result => {
      this._isDesktopDevice = !result.matches;
    })
  }
}
