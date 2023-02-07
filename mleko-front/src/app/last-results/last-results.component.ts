import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MlekoService} from "../services/mleko.service";
import {Game} from "../models/Game";
import {ResultPlayer} from "../models/ResultPlayer";
import {MatTableDataSource} from "@angular/material/table";

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
  dataSource!: MatTableDataSource<ProcessedGame>;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  constructor(
    private mlekoService: MlekoService,
  ) {
  }

  ngOnInit(): void {
    this.mlekoService.getGames(2147483647).subscribe((games: Game[]) => {
      this.games = games
      let processedGames = this.games.map(game => ({
        date: game.reportedTime,
        resultTable: this.generateText(game.ranking),
        reportedBy: game.reportedBy.name,
      }))
      this.dataSource = new MatTableDataSource<ProcessedGame>(processedGames);
      this.dataSource.paginator = this.paginator;
    });
  }

  generateText(ranking: ResultPlayer[][]): string[] {
    let resultTexts: string [];
    resultTexts = [];
    ranking.flatMap(r => r).forEach(resultPlayer => {
      let delta: number = resultPlayer.elo - resultPlayer.preElo;
      let arrow = delta < 0 ? '\u{25b2}' : '\u{25bc}';
      resultTexts.push(`${resultPlayer.place}. ${resultPlayer.name} (${resultPlayer.preElo} -> ${resultPlayer.elo}) ${arrow}${delta}`);
    })
    return resultTexts;
  }

}
