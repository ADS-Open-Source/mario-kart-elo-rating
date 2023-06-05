export interface Player {
  uuid: string;
  name: string;
  elo: number;
  gamesPlayed: number;
  email: string;
  activated: boolean;
  icon?: string;
}

export interface PlayerShort {
  "name": string;
  "email": string;
}

export interface PlayerSecret {
  "secret": string;
}

export interface Result {
  reportedBy: PlayerSecret;
  ranking: Array<Array<Player>>
}
