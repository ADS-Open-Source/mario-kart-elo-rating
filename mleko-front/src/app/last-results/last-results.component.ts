import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MlekoService} from "../services/mleko.service";
import {Game} from "../models/Game";
import {ResultPlayer} from "../models/ResultPlayer";
import {MatTableDataSource} from "@angular/material/table";


export interface ProcessedPlayer {
  text: string;
  eloChange: number;
}

export interface ProcessedGame {
  date: string;
  resultTable: ProcessedPlayer[];
  reportedBy: string;
}

@Component({
  selector: 'app-last-results',
  templateUrl: './last-results.component.html',
  styleUrls: ['./last-results.component.css']
})
export class LastResultsComponent implements OnInit {

  displayedColumns: string[] = ['date', 'result', 'reportedBy'];
  dataSource!: MatTableDataSource<ProcessedGame>;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  constructor(
    private mlekoService: MlekoService,
  ) {
  }

  ngOnInit(): void {
    this.mlekoService.getGames(2147483647).subscribe((games: Game[]) => {
      let processedGames = games.map(game => ({
        date: game.reportedTime,
        resultTable: this.processPlayers(game.ranking),
        reportedBy: game.reportedBy.name,
      }))
      this.dataSource = new MatTableDataSource<ProcessedGame>(processedGames);
      this.dataSource.paginator = this.paginator;
    });
  }

  processPlayers(ranking: ResultPlayer[][]): ProcessedPlayer[] {
    let processedPlayers: ProcessedPlayer [] = [];
    ranking.flatMap(r => r).forEach(resultPlayer => {
      let delta: number = resultPlayer.elo - resultPlayer.preElo;
      let arrow = delta < 0 ? '\u{25bc}' : '\u{25b2}';
      let text = `${resultPlayer.place}. ${resultPlayer.name} (${resultPlayer.preElo} -> ${resultPlayer.elo}) ${arrow}${delta}`;
      processedPlayers.push({text: text, eloChange: delta});
    })
    return processedPlayers;
  }

}
