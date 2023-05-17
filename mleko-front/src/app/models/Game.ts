import {ResultPlayer} from "./ResultPlayer";

export interface ProcessedPlayer {
  text: string;
  eloChange: number;
}

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

  public static processPlayers(ranking: ResultPlayer[][]): ProcessedPlayer[] {
    let processedPlayers: ProcessedPlayer [] = [];
    ranking.flatMap(r => r).sort((a, b) => a.place - b.place).forEach(resultPlayer => {
      let delta: number = resultPlayer.elo - resultPlayer.preElo;
      let arrow = delta < 0 ? '\u{25bc}' : '\u{25b2}';
      let text = `${resultPlayer.place}. ${resultPlayer.name} (${resultPlayer.preElo} -> ${resultPlayer.elo}) ${arrow}${delta}`;
      processedPlayers.push({text: text, eloChange: delta});
    })
    return processedPlayers;
  }
}
