import {Injectable} from '@angular/core';
import {Game} from "../model/Game";
import {Player} from "../model/Player";
import {Result} from "../model/Result";

@Injectable({
  providedIn: 'root'
})
export class MlekoService {

  constructor() {
  }

  getGames(count: number): Game[] {
    //TODO call backend
    let marek = new Player("uuid", "name", 1234, 3, "email");
    let andrzej = new Player("uuid", "name", 999, 3, "email");
    return [new Game("20-12-2022", marek, [[new Result(marek, 1233, 1234)], [new Result(andrzej, 1000, 999)]])];
  }
}
