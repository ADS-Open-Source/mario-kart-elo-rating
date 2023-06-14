import {Component, OnInit} from '@angular/core';
import {Subscription} from "rxjs";
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";
import {MatTableDataSource} from "@angular/material/table";
import {SecretService} from "../services/secret.service";
import {ScreenSizeService} from "../services/screen-size-service.service";
import {MatSnackBar} from "@angular/material/snack-bar";
import {MatDialog} from "@angular/material/dialog";
import {ChangeIconDialogComponent} from "./change-icon-dialog/change-icon-dialog.component";
import {Game} from "../models/Game";

@Component({
  selector: 'app-players',
  templateUrl: './players.component.html',
  styleUrls: ['./players.component.css']
})
export class PlayersComponent implements OnInit {

  playersSub!: Subscription;
  allPlayers: Player[] = [];
  allGames: Game[] = [];
  dataSource: MatTableDataSource<Player> = new MatTableDataSource<Player>();
  displayedColumns: string[] = ['username', 'resend'];
  isProcessing: boolean = false;
  currentUser: Player | null = null;
  playerIcon: string = 'assets/player-icons/0.png';

  constructor(
    private mlekoService: MlekoService,
    private _snackBar: MatSnackBar,
    protected secretService: SecretService,
    protected screenService: ScreenSizeService,
    public changeIconDialog: MatDialog,
  ) {
  }


  ngOnInit(): void {
    this.updateAllPlayers();
    this.secretService.$currentUserStore
      .subscribe((user: Player) => {
        this.currentUser = user;
        this.playerIcon = !user.icon ? this.playerIcon : user.icon;
        this.mlekoService.getGamesByPlayer(this.secretService.secret, null)
          .subscribe(games => {
            this.allGames = games;
          })
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

  openIconPickerDialog(player: Player) {
    this.changeIconDialog.open(ChangeIconDialogComponent, {
      data: {
        user: player,
      }
    })
  }
}
