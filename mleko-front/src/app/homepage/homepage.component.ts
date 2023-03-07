import {Component, OnDestroy, OnInit} from '@angular/core';
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormGroup} from "@angular/forms";
import {NavigationExtras, Router} from "@angular/router";
import {SecretService} from "../services/secret.service";
import {Subscription} from "rxjs";
import {ScreenSizeService} from "../services/screen-size-service.service";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit, OnDestroy {

  playersSub!: Subscription;
  players: Array<Player> = [];
  registrationForm: FormGroup;
  isRegistering: boolean = false;
  errorText: string = "";

  constructor(
    private mlekoService: MlekoService,
    private secretService: SecretService,
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private router: Router,
    private fb: FormBuilder,
    protected screenService: ScreenSizeService
  ) {
    // icons
    this.matIconRegistry.addSvgIcon(
      'trophy',
      this.domSanitizer.bypassSecurityTrustResourceUrl('assets/trophy.svg')
    );
    this.matIconRegistry.addSvgIcon(
      'arrow',
      this.domSanitizer.bypassSecurityTrustResourceUrl('assets/arrow.svg')
    );
    this.matIconRegistry.addSvgIcon(
      'finger',
      this.domSanitizer.bypassSecurityTrustResourceUrl('assets/finger.svg')
    );
    // else
    this.registrationForm = this.fb.group({
      username: '',
      email: '',
    });
  }

  ngOnInit(): void {
    this.playersSub = this.mlekoService.$playersStore
      .subscribe((players: Player[]) => this.players = players.slice(0, 3));
  }

  ngOnDestroy(): void {
    this.playersSub.unsubscribe();
  }

  submitForm() {
    this.isRegistering = true;
    const formData = this.registrationForm.value;
    this.mlekoService.createPlayer(
      {name: formData.username, email: formData.email}
    ).subscribe({
      next: (response) => {
        this.errorText = "";
        console.log("user added");
        this.isRegistering = false;
      },
      error: (error) => {
        console.error(error);
        this.errorText = error.error.replace(/_/g, ' ');
        this.isRegistering = false;
      },
    });
  }

  navigate(url: string) {
    const navExtras: NavigationExtras = {
      queryParams: {secret: this.secretService.secret},
    };
    this.router.navigate([url], navExtras)
  }
}
