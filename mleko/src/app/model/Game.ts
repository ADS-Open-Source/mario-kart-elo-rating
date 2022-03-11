
import {Result} from "./Result";
import {Player} from "src/app/model/Player";

export class Game {
  private readonly _reportedTime:string;
  private readonly _reportedBy:Player;
  private readonly _result:Result[][];

  constructor(reportedTime: any, player: Player, result: Result[][]) {
    this._reportedTime = reportedTime;
    this._reportedBy = player;
    this._result = result;
  }

  get reportedTime(): string {
    return this._reportedTime;
  }

  get reportedBy(): Player {
    return this._reportedBy;
  }

  get result(): Result[][] {
    return this._result;
  }
}
