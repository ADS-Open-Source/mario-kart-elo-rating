import { Injectable } from '@angular/core';
import {Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class SecretService {

  private _secret: string = '';

  constructor() { }

  public get secret(): string {
    return this._secret;
  }

  public set secret(value: string) {
    this._secret = value;
  }

}
