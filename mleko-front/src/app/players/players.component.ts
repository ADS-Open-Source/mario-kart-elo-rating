import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.css']
})
export class PlayersComponent implements OnInit {

  playersSub!: Subscription;
  allPlayers: Player[] = [];

  constructor(
    private mlekoService: MlekoService,
  ) {
  }

  updateAllPlayers(): void {
    this.playersSub = this.mlekoService.$playersStore
      .subscribe((players: Player[]) => {
        this.allPlayers = players.sort((a, b) => a.name.localeCompare(b.name));
      });
  }

  ngOnInit(): void {
    this.updateAllPlayers();
  }

}
