import {Injectable} from '@angular/core';
import {Game} from "../model/Game";
import {HttpClient} from "@angular/common/http";
import {Observable} from "rxjs";
import {Player, Result} from "src/app/model/models";

@Injectable({
  providedIn: 'root'
})
export class MlekoService {

  private DOMAIN = 'http://mleko.deloitte.com.pl/api/'

  constructor(
    private httpClient: HttpClient
  ) {
  }

  getPlayers(): Observable<Array<Player>> {
    return this.httpClient.get<Array<Player>>(this.DOMAIN + 'player');
  }

  saveResult(result: Result): Observable<any> {
    return this.httpClient.post<Observable<any>>(this.DOMAIN + 'game', result);
  }

  getGames(count: number): Game[] {
    return [];
  }
}
