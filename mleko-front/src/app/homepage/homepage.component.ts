import {Component, OnDestroy, OnInit} from '@angular/core';
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormGroup} from "@angular/forms";
import {NavigationExtras, Router} from "@angular/router";
import {SecretService} from "../services/secret.service";
import {Subscription} from "rxjs";

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

  constructor(
    private mlekoService: MlekoService,
    private secretService: SecretService,
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private router: Router,
    private fb: FormBuilder,
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
        console.log("user added")
      },
      error: (error) => {
        console.error(error)
      },
      complete: () => {
        this.isRegistering = false;
      }
    });
  }

  navigate(url: string) {
    const navExtras: NavigationExtras = {
      queryParams: {secret: this.secretService.secret},
    };
    this.router.navigate([url], navExtras)
  }
}
