import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";
import {MatTableDataSource} from "@angular/material/table";
import {SecretService} from "../services/secret.service";
import {ScreenSizeService} from "../services/screen-size-service.service";

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
    private secretService: SecretService,
    protected screenService: ScreenSizeService,
  ) {
  }

  updateAllPlayers(): void {
    this.playersSub = this.mlekoService.$playersStore
      .subscribe((players: Player[]) => {
        this.allPlayers = players;
        this.allPlayers.sort((a, b) => a.name.localeCompare(b.name));
        this.dataSource.data = this.allPlayers;
      });
  }

  ngOnInit(): void {
    this.updateAllPlayers();
  }

  resendEmail(username: string) {
    this.isProcessing = true;
    this.mlekoService.resendMail(
      this.secretService.secret,
      {name: username, email: ""}
    ).subscribe({
      next: (response) => {
        console.log("email resent")
        this.isProcessing = false;
      },
      error: (error) => {
        console.error(error)
        this.isProcessing = false;
      }
    })
  }
}
