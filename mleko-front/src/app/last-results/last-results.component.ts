import {Component, OnInit} from '@angular/core';
import {MlekoService} from "../services/mleko.service";
import {Game} from "../models/Game";
import {ResultPlayer} from "../models/ResultPlayer";

export interface ProcessedGame {
  date: string;
  resultTable: string[];
  reportedBy: string;
}

@Component({
  selector: 'app-last-results',
  templateUrl: './last-results.component.html',
  styleUrls: ['./last-results.component.css']
})
export class LastResultsComponent implements OnInit {

  displayedColumns: string[] = ['date', 'result', 'reportedBy'];
  games: Array<Game> = [];
  dataSource: ProcessedGame[] = [];

  constructor(
    private mlekoService: MlekoService,
  ) {
  }

  ngOnInit(): void {
    this.mlekoService.getGames(10).subscribe((games: Game[]) => {
      this.games = games
      this.dataSource = this.games.map(game => ({
        date: game.reportedTime,
        resultTable: this.generateText(game.ranking),
        reportedBy: game.reportedBy.name,
      }))
    });
  }

  generateText(ranking: ResultPlayer[][]): string[] {
    let resultTexts: string [];
    resultTexts = [];
    for (const array of ranking) {
      for (const resultPlayer of array) {
        let delta: number = resultPlayer.elo - resultPlayer.preElo;
        let arrow = '\u{25b2}'
        if (delta < 0) {
          arrow = '\u{25bc}'
        }
        resultTexts.push(`${resultPlayer.place}. ${resultPlayer.name} (${resultPlayer.preElo} -> ${resultPlayer.elo}) ${arrow}${delta}`);
      }
    }
    return resultTexts;
  }

}
