import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.css']
})
export class PlayersComponent implements OnInit {

  playersSub!: Subscription;
  allPlayers: Player[] = [];
  dataSource: MatTableDataSource<Player> = new MatTableDataSource<Player>();
  displayedColumns: string[] = ['username', 'resend'];
  isProcessing: boolean = false;

  constructor(
    private mlekoService: MlekoService,
  ) {
  }

  updateAllPlayers(): void {
    this.playersSub = this.mlekoService.$playersStore
      .subscribe((players: Player[]) => {
        this.allPlayers = players.sort((a, b) => a.name.localeCompare(b.name));
        this.dataSource.data = this.allPlayers;
      });
  }

  ngOnInit(): void {
    this.updateAllPlayers();
  }

  resendEmail(username: string) {
    this.isProcessing = true;
    console.log(username)// TODO finish resend Email
  }
}
