import {Injectable} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Player} from "../models/Player";
import {Game} from "../models/Game";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MlekoService {

  private static BACKEND_DOMAIN = environment.apiURL;

  constructor(
    private httpClient: HttpClient
  ) {
  }

  getPlayers(): Observable<Array<Player>> {
    return this.httpClient.get<Array<Player>>(`${MlekoService.BACKEND_DOMAIN}/players/all`);
  }

  getGames(number: number): Observable<Array<Game>> {
    return this.httpClient.get<Array<Game>>(`${MlekoService.BACKEND_DOMAIN}/games?count=${number}`);
  }

  getAllGames(): Observable<Array<Game>> {
    return this.httpClient.get<Array<Game>>(`${MlekoService.BACKEND_DOMAIN}/games/all`)
  }
}
