import {Injectable} from '@angular/core';
import {MlekoService} from "./mleko.service";

@Injectable({
  providedIn: 'root'
})
export class SecretService {

  private _secret: string = '';
  private _isActivated: boolean = false;

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

  public checkIfActivated(): void {
    if (this.secret != '') {
      this.mlekoService.isActivated(this.secret)
        .subscribe({
          next: (res) => {
            this._isActivated = res
          }
        })
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
          }
        });
    }
  }

}
