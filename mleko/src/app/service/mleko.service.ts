import {Injectable} from '@angular/core';
import {Game} from "../model/Game";
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {Player, PlayerSecret, PlayerShort, Result} from "src/app/model/models";

@Injectable({
  providedIn: 'root'
})
export class MlekoService {

  private DOMAIN = 'http://mleko.dolittle.com.pl/api/'

  constructor(
    private httpClient: HttpClient
  ) {
  }

  getPlayers(): Observable<Array<Player>> {
    return this.httpClient.get<Array<Player>>(this.DOMAIN + 'players/all');
  }

  activatePlayer(playerSecret: PlayerSecret): Observable<any> {
    return this.httpClient.post<Observable<any>>(`${this.DOMAIN}players/activate`, playerSecret);
  }

  isActivated(secret: string): Observable<any> {
    return this.httpClient.get<Observable<boolean>>(`${this.DOMAIN}players/activated/${secret}`)
  }

  saveResult(result: Result): Observable<any> {
    const headers = new HttpHeaders().set('Accept', 'text/plain');
    return this.httpClient.post(this.DOMAIN + 'games', result, {headers: headers, responseType: 'text'});
  }

  getGames(count: number): Observable<Array<Game>> {
    return this.httpClient.get<Array<Game>>(this.DOMAIN + 'games?count=' + count);
  }

  savePlayer(playerShort: PlayerShort): Observable<any> {
    return this.httpClient.post<Observable<any>>(this.DOMAIN + 'players', playerShort);
  }
}
