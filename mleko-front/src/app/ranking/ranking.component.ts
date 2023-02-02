import {Component, OnInit} from '@angular/core';
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";

@Component({
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  styleUrls: ['./ranking.component.css']
})
export class RankingComponent implements OnInit {

  players: Array<Player> = [];
  displayedColumns: string[] = ['position', 'username', 'gamesPlayed', 'elo'];

  constructor(
    private mlekoService: MlekoService,
  ) {
  }

  ngOnInit(): void {
    this.mlekoService.getPlayers()
      .subscribe((players: Player[]) => this.players = players);
  }

}
