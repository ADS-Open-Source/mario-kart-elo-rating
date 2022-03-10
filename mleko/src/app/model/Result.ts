import {Player} from "./Player";

export class Result {
  private readonly _player:Player;
  private readonly _eloBefore:number;
  private readonly _eloAfter:number;

  constructor(player: Player, eloBefore: number, eloAfter: number) {
    this._player = player;
    this._eloBefore = eloBefore;
    this._eloAfter = eloAfter;
  }

  get player(): Player {
    return this._player;
  }

  get eloBefore(): number {
    return this._eloBefore;
  }

  get eloAfter(): number {
    return this._eloAfter;
  }
}
