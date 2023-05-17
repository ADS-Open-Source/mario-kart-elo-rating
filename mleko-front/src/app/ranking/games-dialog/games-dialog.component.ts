import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {Player} from "../../models/Player";
import {ProcessedGame} from "../../models/Game";


export interface DialogData {
  player: Player,
  games: Array<ProcessedGame>
}

@Component({
  selector: 'app-games-dialog',
  templateUrl: './games-dialog.component.html',
  styleUrls: ['./games-dialog.component.css']
})
export class GamesDialogComponent {

  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }
}
