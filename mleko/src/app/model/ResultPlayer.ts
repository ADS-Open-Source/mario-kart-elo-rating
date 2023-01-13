export class ResultPlayer {
  private readonly _uuid: string;
  private readonly _name: string;
  private readonly _elo: number;
  private readonly _preElo: number;
  private readonly _place: number;
  private readonly _gamesPlayed: number;

  constructor(uuid: string, name: string, elo: number, preElo: number, place: number, gamesPlayed: number) {
    this._uuid = uuid;
    this._name = name;
    this._elo = elo;
    this._preElo = preElo;
    this._place = place;
    this._gamesPlayed = gamesPlayed;
  }

  get uuid(): string {
    return this._uuid;
  }

  get name(): string {
    return this._name;
  }

  get elo(): number {
    return this._elo;
  }

  get preElo(): number {
    return this._preElo;
  }

  get place(): number {
    return this._place;
  }

  get gamesPlayed(): number {
    return this._gamesPlayed;
  }

}
