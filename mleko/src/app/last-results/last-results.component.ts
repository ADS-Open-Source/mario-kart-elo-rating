import {Component, OnInit} from '@angular/core';
import {MlekoService} from "../service/mleko.service";
import {Game} from "../model/Game";
import {ResultPlayer} from "../model/ResultPlayer";

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

  constructor(private mlekoService: MlekoService) {
  }

  dataSource: ProcessedGame[] = [
    // {date: "2022-03-10T19:19:15.9821672+01:00", result: "1. Arti (2067 +11), 2. Marek (1933 -11)", reportedBy: "Arti"},
    // {date: "2022-03-10T19:18:56.5413434+01:00", result: "1. Arti (2056 +12), 2. Marek (1944 -12)", reportedBy: "Marek"}
  ];

  displayedColumns: string[] = ['date', 'result', 'reportedBy'];
  games: Array<Game> = [];

  generateText(ranking : ResultPlayer[][]): string[] {
    let resultTexts: string [];
    resultTexts = [];
    for (const array of ranking) {
      for (const resultPlayer of array) {
        resultTexts.push(`${resultPlayer.place}. ${resultPlayer.name} (${resultPlayer.preElo} -> ${resultPlayer.elo} Δ${resultPlayer.elo-resultPlayer.preElo})`);
      }
    }
    return resultTexts;
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

}
