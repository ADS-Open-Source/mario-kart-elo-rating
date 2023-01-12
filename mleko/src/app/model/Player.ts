export class Player {
  private readonly _uuid: string;
  private readonly _name: string;
  private readonly _elo: number;
  private readonly _gamesPlayed: number;
  private readonly _email: string;

  constructor(uuid: string, name: string, elo: number, gamesPlayed: number, email: string) {
    this._uuid = uuid;
    this._name = name;
    this._elo = elo;
    this._gamesPlayed = gamesPlayed;
    this._email = email;
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

  get gamesPlayed(): number {
    return this._gamesPlayed;
  }

  get email(): string {
    return this._email;
  }
}
