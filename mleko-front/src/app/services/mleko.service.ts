import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Player} from "../models/Player";

@Injectable({
  providedIn: 'root'
})
export class MlekoService {

  private static BACKEND_DOMAIN = 'http://localhost:8080/api';

  constructor(
    private httpClient: HttpClient
  ) {
  }

  getPlayers(): Observable<Array<Player>> {
    return this.httpClient.get<Array<Player>>(`${MlekoService.BACKEND_DOMAIN}/players/all`);
  }
}
