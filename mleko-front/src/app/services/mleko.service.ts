import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable, ReplaySubject} from "rxjs";
import {Player, PlayerSecret, PlayerShort, Result} from "../models/Player";
import {Game} from "../models/Game";
import {environment} from "../../environments/environment";

@Injectable({
  providedIn: 'root'
})
export class MlekoService {

  private static BACKEND_DOMAIN = environment.apiURL;
  private _playersStore: ReplaySubject<Player[]> = new ReplaySubject<Player[]>(1);

  constructor(
    private httpClient: HttpClient
  ) {
    this.loadPlayers();
  }

  get $playersStore(): Observable<Player[]> {
    return this._playersStore.asObservable();
  }

  loadPlayers(): void {
    this.httpClient.get<Array<Player>>(`${MlekoService.BACKEND_DOMAIN}/players/all`).subscribe(data => {
      this._playersStore.next(data);
    });
  }
  // GET
  getPlayers(): Observable<Array<Player>> {
    return this.httpClient.get<Array<Player>>(`${MlekoService.BACKEND_DOMAIN}/players/all`);
  }

  getGames(number: number): Observable<Array<Game>> {
    return this.httpClient.get<Array<Game>>(`${MlekoService.BACKEND_DOMAIN}/games?count=${number}`);
  }

  getGamesByPlayer(playerSecret: string, opponentName: string | null): Observable<Array<Game>> {
    if (opponentName) {
      return this.httpClient.get<Array<Game>>(`${MlekoService.BACKEND_DOMAIN}/games/${playerSecret}?opponent=${opponentName}`);
    }
    return this.httpClient.get<Array<Game>>(`${MlekoService.BACKEND_DOMAIN}/games/${playerSecret}`);
  }

  getAllGames(): Observable<Array<Game>> {
    return this.httpClient.get<Array<Game>>(`${MlekoService.BACKEND_DOMAIN}/games/all`)
  }


  isActivated(secret: string): Observable<any> {
    return this.httpClient.get<Observable<boolean>>(`${MlekoService.BACKEND_DOMAIN}/players/activated/${secret}`)
  }

  whoAmI(secret: string): Observable<any> {
    return this.httpClient.get<Observable<Player>>(`${MlekoService.BACKEND_DOMAIN}/players/whoami/${secret}`)
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
  }

  // players
  createPlayer(playerShort: PlayerShort): Observable<any> {
    return this.httpClient.post<Observable<any>>(`${MlekoService.BACKEND_DOMAIN}/players`, playerShort);
  }

  resendMail(requesterSecret: string, playerShort: PlayerShort): Observable<any> {

    return this.httpClient.post<Observable<any>>(`${MlekoService.BACKEND_DOMAIN}/players/resend/${requesterSecret}`, playerShort);
  }

  // PATCH
  changeUserIcon(userSecret: string, iconPath: string): Observable<any> {
    return this.httpClient.patch<Observable<Player>>(`${MlekoService.BACKEND_DOMAIN}/players/${userSecret}/icon`, {
        'icon': iconPath
      })
  }

  //DELETE
  deleteLastGame(requesterSecret: string): Observable<any> {
    const headers: HttpHeaders = new HttpHeaders().set('Accept', 'text/plain')
    return this.httpClient.delete(
      `${MlekoService.BACKEND_DOMAIN}/games/last/${requesterSecret}`,
      {headers: headers, responseType: 'text'}
    )
  }
}
