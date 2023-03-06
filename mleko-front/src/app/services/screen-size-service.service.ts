import {Injectable, OnInit} from '@angular/core';
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";

@Injectable({
  providedIn: 'root'
})
export class ScreenSizeService implements OnInit{
  // TODO Currently it's absolutely useless, I'd like to fix that

  private _isDesktopDevice: boolean = true;

  constructor(
    private responsive: BreakpointObserver,
  ) {
  }

  public get isDesktop(): boolean {
    return this._isDesktopDevice;
  }

  ngOnInit(): void {

    this.responsive.observe([Breakpoints.Handset, Breakpoints.Tablet]).subscribe(result => {
      this._isDesktopDevice = !result.matches;
    })
  }
}
