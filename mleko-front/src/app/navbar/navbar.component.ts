import {Component, OnInit} from '@angular/core';
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import {ActivatedRoute, NavigationEnd, NavigationExtras, Router} from "@angular/router";
import {SecretService} from "../services/secret.service";
import {MlekoService} from "../services/mleko.service";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {ScreenSizeService} from "../services/screen-size-service.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  currentRoute: string = '';
  isActivated: boolean = false;
  isDesktop: boolean = true;

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private router: Router,
    private route: ActivatedRoute,
    private secretService: SecretService,
    private mlekoService: MlekoService,
    private screenService: ScreenSizeService,
    private responsive: BreakpointObserver,
  ) {
    this.matIconRegistry.addSvgIcon(
      'cloud',
      this.domSanitizer.bypassSecurityTrustResourceUrl('assets/cloud.svg')
    );
  }

  ngOnInit(): void {
    this.route.queryParamMap.subscribe(params => {
      let secret = params.get('secret')
      this.secretService.secret = secret || '';
      // console.log('initial', this.secretService.secret); // TODO why is it being run twice
      if (secret) {
        this.mlekoService.isActivated(secret).subscribe(isActive => {
          this.isActivated = isActive;
        });
      }
    });

    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.currentRoute = window.location.pathname;
      }
    })

    this.responsive.observe([Breakpoints.HandsetPortrait]).subscribe(result => {
      this.isDesktop = !result.matches;
      this.screenService.isDesktop = !result.matches;
    })
  }

  navigate(url: string) {
    const navExtras: NavigationExtras = {
      queryParams: {secret: this.secretService.secret},
    };
    this.router.navigate([url], navExtras)
  }
}
