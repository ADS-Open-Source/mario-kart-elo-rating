import {Component, OnInit} from '@angular/core';
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import {FormBuilder, FormGroup} from "@angular/forms";

@Component({
  selector: 'app-homepage',
  templateUrl: './homepage.component.html',
  styleUrls: ['./homepage.component.css']
})
export class HomepageComponent implements OnInit {

  players: Array<Player> = [];
  registrationForm: FormGroup;
  isRegistering: boolean = false;

  constructor(
    private mlekoService: MlekoService,
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
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
    this.mlekoService.getPlayers()
      .subscribe((players: Player[]) => this.players = players.slice(0, 3));
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
}
