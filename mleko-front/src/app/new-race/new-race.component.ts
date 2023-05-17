import {Component, OnDestroy, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {Player, Result} from "../models/Player";
import {MlekoService} from "../services/mleko.service";
import {MatSelectChange} from "@angular/material/select";
import {SecretService} from "../services/secret.service";
import {NavigationExtras, Router} from "@angular/router";
import {Subscription} from "rxjs";

@Component({
  selector: 'app-new-race',
  templateUrl: './new-race.component.html',
  styleUrls: ['./new-race.component.css'],
})
export class NewRaceComponent implements OnInit, OnDestroy {

  playersSub!: Subscription;
  raceForm: FormGroup;
  allPlayers: Player[] = [];
  isActivated: boolean = false;


  constructor(
    private fb: FormBuilder,
    private mlekoService: MlekoService,
    protected secretService: SecretService,
    private router: Router,
  ) {
    this.raceForm = this.fb.group({
      firstPlace: new Array<Player>(),
      secondPlace: new Array<Player>(),
      thirdPlace: new Array<Player>(),
      fourthPlace: new Array<Player>(),
    })
  }

  updateAllPlayers(): void {
    this.playersSub = this.mlekoService.$playersStore
      .subscribe((players: Player[]) => {
        this.allPlayers = players;
      });
  }

  ngOnInit(): void {
    this.updateAllPlayers();

    // handle activation
    this.secretService.activatePlayer();
  }

  ngOnDestroy(): void {
    this.playersSub.unsubscribe();
  }


  onSelectionChange(event: MatSelectChange, formControlName: string) {
    this.raceForm.controls[formControlName].setValue(
      event.value.filter((player: Player) =>
        this.chosenPlayers.filter(chosenPlayer =>
          chosenPlayer == player).length <= 1)
    )
  }

  get chosenPlayers(): Player[] {
    return Object.values(this.raceForm.controls).map(control => control.value).flat();
  }

  submitRace(): void {
    const formValue = this.raceForm.value
    const result: Result = {
      reportedBy: {
        secret: this.secretService.secret
      },
      ranking: [
        formValue.firstPlace || [],
        formValue.secondPlace || [],
        formValue.thirdPlace || [],
        formValue.fourthPlace || [],
      ]
    }

    console.log(result)
    if (this.chosenPlayers.filter(p => p != null).length >= 2) {
      this.mlekoService.saveResult(result)
        .subscribe((res: { text: string; }) => {
          console.log(res)
          const navExtras: NavigationExtras = {
            queryParams: {secret: this.secretService.secret},
          };
          this.router.navigate(['last-races'], navExtras)
        })
    }
  }
}
