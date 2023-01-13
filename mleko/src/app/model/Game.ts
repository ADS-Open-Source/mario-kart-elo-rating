import {ResultPlayer} from "./ResultPlayer";

export class Game {
  private readonly _reportedTime: string;
  private readonly _reportedBy: ResultPlayer;
  private readonly _ranking: ResultPlayer[][];

  constructor(reportedTime: any, player: ResultPlayer, resultPlayers: ResultPlayer[][]) {
    this._reportedTime = reportedTime;
    this._reportedBy = player;
    this._ranking = resultPlayers;
  }

  get reportedTime(): string {
    return this._reportedTime;
  }

  get reportedBy(): ResultPlayer {
    return this._reportedBy;
  }

  get ranking(): ResultPlayer[][] {
    return this._ranking;
  }
}
