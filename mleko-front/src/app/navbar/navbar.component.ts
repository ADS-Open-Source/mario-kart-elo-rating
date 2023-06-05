import {Component, OnInit} from '@angular/core';
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import {ActivatedRoute, NavigationEnd, NavigationExtras, Router} from "@angular/router";
import {SecretService} from "../services/secret.service";
import {MlekoService} from "../services/mleko.service";
import {BreakpointObserver, Breakpoints} from "@angular/cdk/layout";
import {ScreenSizeService} from "../services/screen-size-service.service";
import {Player} from "../models/Player";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  currentRoute: string = '';
  currentUser: Player | null = null;
  isActivated: boolean = false;
  isDesktop: boolean = true;
  playerIconPath: string = 'assets/player-icons/0.png';

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
      this.secretService.secret = params.get('secret') || '';
      // console.log('initial', this.secretService.secret); // TODO why is it being run twice
      this.secretService.updateCurrentUser();
      this.secretService.$currentUserStore
        .subscribe((user: Player) => {
          this.currentUser = user;
        })
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
