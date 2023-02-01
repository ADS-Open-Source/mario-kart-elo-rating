import {Component, OnInit} from '@angular/core';
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import {ActivatedRoute, NavigationEnd, NavigationExtras, Router} from "@angular/router";
import {SecretService} from "../secret.service";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {

  currentRoute: string = '';

  constructor(
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private router: Router,
    private route: ActivatedRoute,
    private secretService: SecretService,
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
    });

    this.router.events.subscribe((event) => {
      if (event instanceof NavigationEnd) {
        this.currentRoute = window.location.pathname;
      }
    })
  }

  navigate(url: string) {
    const navExtras: NavigationExtras = {
      queryParams: {secret: this.secretService.secret},
    };
    this.router.navigate([url], navExtras)
  }
}
