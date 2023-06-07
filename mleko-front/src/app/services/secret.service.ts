import {Injectable} from '@angular/core';
import {MlekoService} from "./mleko.service";
import {Observable, ReplaySubject} from "rxjs";
import {Player} from "../models/Player";

@Injectable({
  providedIn: 'root'
})
export class SecretService {

  public static UUIDRegex: RegExp = new RegExp('^[0-9a-f]{8}-[0-9a-f]{4}-[0-5][0-9a-f]{3}-[089ab][0-9a-f]{3}-[0-9a-f]{12}$', 'i')
  private _secret: string = '';
  private _isActivated: boolean = false;
  private _isActivatedStore: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);
  private _currentUserStore: ReplaySubject<Player> = new ReplaySubject<Player>(1);


  constructor(
    private mlekoService: MlekoService,
  ) {
  }

  public get secret(): string {
    return this._secret;
  }

  public set secret(value: string) {
    this._secret = value;
  }

  public get isActivated(): boolean {
    return this._isActivated;
  }

  get $isActivatedStore(): Observable<boolean> {
    return this._isActivatedStore.asObservable();
  }

  get $currentUserStore(): Observable<Player> {
    return this._currentUserStore.asObservable();
  }

  public checkIfActivated(): void {
    if (this.secret != '') {
      this.mlekoService.isActivated(this.secret)
        .subscribe(
          data => {
            this._isActivatedStore.next(data);
          }
        )
    }
  }

  public updateCurrentUser(): void {
    if (SecretService.UUIDRegex.test(this.secret)) {
      this.mlekoService.whoAmI(this.secret)
        .subscribe(
          data => {
            let player: Player = data;
            if (!player.icon) {
              player.icon = MlekoService.getSeededImagePath(player,'assets/player-icons', 72);
            }
            this._currentUserStore.next(player);
          }
        )
    }
  }

  public activatePlayer(): void {
    if (this.secret != '') {
      this.mlekoService.isActivated(this.secret)
        .subscribe({
          next: (response) => {
            this._isActivated = response;
            if (!response) {
              this.mlekoService.activatePlayer({secret: this.secret})
                .subscribe({
                  next: (response) => {
                    console.log(response);
                  },
                  complete: () => {
                    this.mlekoService.loadPlayers();
                  }
                });
            }
            this.mlekoService.loadPlayers();
          }
        });
    }
  }
}
