export interface Player {
  uuid: string;
  name: string;
  elo: number;
  gamesPlayed: number;
  email: string;
}

export interface Result {
  reportedBySecret: string;
  results: Array<Array<string>>
}

export interface PlayerShort {
  "name": string;
  "email": string;
}
