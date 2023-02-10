import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Player, PlayerSecret, Result} from "../models/Player";
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

  // GET
  getPlayers(): Observable<Array<Player>> {
    return this.httpClient.get<Array<Player>>(`${MlekoService.BACKEND_DOMAIN}/players/all`);
  }

  getGames(number: number): Observable<Array<Game>> {
    return this.httpClient.get<Array<Game>>(`${MlekoService.BACKEND_DOMAIN}/games?count=${number}`);
  }

  getAllGames(): Observable<Array<Game>> {
    return this.httpClient.get<Array<Game>>(`${MlekoService.BACKEND_DOMAIN}/games/all`)
  }


  isActivated(secret: string): Observable<any> {
    return this.httpClient.get<Observable<boolean>>(`${MlekoService.BACKEND_DOMAIN}/players/activated/${secret}`)
  }

  //POST
  activatePlayer(playerSecret: PlayerSecret): Observable<any> {
    const headers = new HttpHeaders().set('Accept', 'text/plain');
    return this.httpClient.post(
      `${MlekoService.BACKEND_DOMAIN}/players/activate`,
      playerSecret,
      {headers: headers, responseType: 'text'}
    );
  }

  saveResult(result: Result): Observable<any> {
    const headers = new HttpHeaders().set('Accept', 'text/plain');
    return this.httpClient.post(
      `${MlekoService.BACKEND_DOMAIN}/games`,
      result,
      {headers: headers, responseType: 'text'}
    );

  // players
  createPlayer(playerShort: PlayerShort): Observable<any> {
    return this.httpClient.post<Observable<any>>(`${MlekoService.BACKEND_DOMAIN}/players`, playerShort);

  }
}
