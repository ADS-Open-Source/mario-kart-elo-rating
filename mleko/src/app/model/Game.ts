
import {Result} from "./Result";
import {Player} from "src/app/model/Player";

export class Game {
  private readonly _reportedTime:any;
  private readonly _player:Player;
  private readonly _result:Result[][];

  constructor(reportedTime: any, player: Player, result: Result[][]) {
    this._reportedTime = reportedTime;
    this._player = player;
    this._result = result;
  }

  get reportedTime(): any {
    return this._reportedTime;
  }

  get player(): Player {
    return this._player;
  }

  get result(): Result[][] {
    return this._result;
  }
}
