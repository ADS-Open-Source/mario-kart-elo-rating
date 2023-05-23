import {Injectable} from '@angular/core';
import {MlekoService} from "./mleko.service";
import {Observable, ReplaySubject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SecretService {

  private _secret: string = '';
  private _isActivated: boolean = false;
  private _isActivatedStore: ReplaySubject<boolean> = new ReplaySubject<boolean>(1);

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
