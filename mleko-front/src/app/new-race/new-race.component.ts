import {Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {Player, Result} from "../models/Player";
import {FormControl} from "@angular/forms";
import {ReplaySubject, Subject, Subscription, takeUntil} from "rxjs";
import {MatSelect} from "@angular/material/select";
import {SecretService} from "../services/secret.service";
import {MlekoService} from "../services/mleko.service";
import {NavigationExtras, Router} from "@angular/router";

@Component({
  selector: 'app-new-race',
  templateUrl: './new-race.component.html',
  styleUrls: ['./new-race.component.css'],
})
export class NewRaceComponent implements OnInit, OnDestroy {

  playersSub!: Subscription;
  isActivated: boolean = false;
  protected players!: Player[];
  public playerFirstMultiCtrl: FormControl<Player[] | null> = new FormControl<Player[]>([]);
  public playerSecondMultiCtrl: FormControl<Player[] | null> = new FormControl<Player[]>([]);
  public playerThirdMultiCtrl: FormControl<Player[] | null> = new FormControl<Player[]>([]);
  public playerFourthMultiCtrl: FormControl<Player[] | null> = new FormControl<Player[]>([]);
  //@ts-ignore
  public playerMultiFilterCtrl: FormControl<string> = new FormControl<string>('');
  public filteredPlayersMulti: ReplaySubject<Player[]> = new ReplaySubject<Player[]>(1);

  @ViewChild('multiSelect', {static: true}) multiSelect!: MatSelect;

  protected _onDestroy = new Subject<void>();

  constructor(
    private mlekoService: MlekoService,
    protected secretService: SecretService,
    private router: Router,
  ) {
  }

  updateAllPlayers(): void {
    this.playersSub = this.mlekoService.$playersStore
      .subscribe((players: Player[]) => {
        this.players = players;
      });
  }

  ngOnInit() {
    // handle activation
    const playerSecret = this.secretService.secret;
    if (playerSecret != '') {
      this.mlekoService.isActivated(playerSecret)
        .subscribe({
          next: (response) => {
            this.isActivated = response;
            if (!response) {
              this.mlekoService.activatePlayer({secret: playerSecret})
                .subscribe({
                  next: (response) => {
                    console.log(response);
                    this.updateAllPlayers();
                  },
                  complete: () => {
                    this.mlekoService.loadPlayers();
                  }
                });
            }
          }
        });
    }
    this.updateAllPlayers();  // TODO this part is not working properly for some reason
    // skip initial selection
    // load initial bank list
    this.filteredPlayersMulti.next(this.players.slice());
    // listen for the search field value change
    this.playerMultiFilterCtrl.valueChanges
      .pipe(takeUntil(this._onDestroy))
      .subscribe(() => {
        this.filterPlayersMulti();
      });
  }

  ngOnDestroy() {
    this._onDestroy.next();
    this._onDestroy.complete();
  }

  protected filterPlayersMulti() {
    if (!this.players) {
      return;
    }
    // get the search
    let search = this.playerMultiFilterCtrl.value;
    if (!search) {
      this.filteredPlayersMulti.next(this.players.slice());
      return;
    } else {
      search = search.toLowerCase();
    }
    // filter the players
    this.filteredPlayersMulti.next(
      this.players.filter(player => player.name.toLowerCase().indexOf(search) > -1)
    );
  }

  submitRace(): void {
    const result: Result = {
      reportedBy: {
        secret: this.secretService.secret
      },
      ranking: [
        this.playerFirstMultiCtrl.value || [],
        this.playerSecondMultiCtrl.value || [],
        this.playerThirdMultiCtrl.value || [],
        this.playerFourthMultiCtrl.value || [],
      ]
    }

    console.log(result)
    // if (this.chosenPlayers.filter(p => p != null).length >= 2) {
    if (true) {  // TODO obviously change that
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
