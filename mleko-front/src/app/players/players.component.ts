import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";
import {MatTableDataSource} from "@angular/material/table";
import {SecretService} from "../services/secret.service";
import {ScreenSizeService} from "../services/screen-size-service.service";
import {MatSnackBar} from "@angular/material/snack-bar";

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
  currentUser: Player | null = null;

  constructor(
    private mlekoService: MlekoService,
    protected secretService: SecretService,
    protected screenService: ScreenSizeService,
    private _snackBar: MatSnackBar,
  ) {
  }


  ngOnInit(): void {
    this.updateAllPlayers();
    this.secretService.$currentUserStore
      .subscribe((user: Player) => {
        this.currentUser = user;
      })
  }

  updateAllPlayers(): void {
    this.playersSub = this.mlekoService.$playersStore
      .subscribe((players: Player[]) => {
        players.sort((a, b) => a.name.localeCompare(b.name));
        this.allPlayers = players;
        this.dataSource.data = this.allPlayers;
      });
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
        this._snackBar.open('Email resent successfully!', 'Close', {duration: 3000})
      },
      error: (error) => {
        console.error(error)
        this.isProcessing = false;
        this._snackBar.open(error.error, 'Close', {duration: 5000})
      }
    })
  }
}
