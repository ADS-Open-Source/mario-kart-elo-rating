import {Component, OnInit} from '@angular/core';
import {MlekoService} from "../service/mleko.service";
import {Game} from "../model/Game";
import {Player} from "../model/models";
import {Result} from "../model/Result";

export interface ProcessedGame {
  date: string;
  result: string;
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

  generateText(results : Result[][]): string {
    let i = 0;
    let text = "";
    for (const array of results) {
      i++;
      for (const result of array) {
        text += " " + i + ". " + result.player.name + " (" + (result.eloAfter - result.eloBefore) + " -> "
          + result.eloAfter + ")";
      }
    }
    return text;
  }

  ngOnInit(): void {
    this.mlekoService.getGames(10).subscribe(games => {
      this.games = games
      console.log(this.games);
      this.dataSource = []
      for (const game of this.games) {
        this.dataSource.push({
          date: game.reportedTime,
          result: this.generateText(game.result),
          reportedBy: game.reportedBy.name
        })
      }
    });
  }

}
