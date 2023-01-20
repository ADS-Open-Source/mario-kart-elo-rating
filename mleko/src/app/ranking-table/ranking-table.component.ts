import {Component, OnInit} from '@angular/core';
import {MlekoService} from "../service/mleko.service";
import {Player} from "../model/models";


@Component({
  selector: 'app-ranking-table',
  templateUrl: './ranking-table.component.html',
  styleUrls: ['../app.component.css', './ranking-table.component.css']
})
export class RankingTableComponent implements OnInit {

  displayedColumns: string[] = ['position', 'name', 'gamesPlayed', 'elo'];
  players: Array<Player> = [];

  constructor(
    private mlekoService: MlekoService,
  ) {
  }

  ngOnInit(): void {
    this.mlekoService.getPlayers()
      .subscribe((players: Player[]) => this.players = players);
  }

}
