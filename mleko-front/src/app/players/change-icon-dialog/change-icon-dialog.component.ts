import {Component, Inject} from '@angular/core';
import {Player} from "../../models/Player";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";

export interface DialogData {
  user: Player,
}

@Component({
  selector: 'app-change-icon-dialog',
  templateUrl: './change-icon-dialog.component.html',
  styleUrls: ['./change-icon-dialog.component.css']
})
export class ChangeIconDialogComponent {

  iconsPaths: string[];
  selectedIcon: string;

  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {
    this.iconsPaths = Array(72).fill(1).map((x, i) => `assets/player-icons/${i + 1}.png`)
    this.selectedIcon = data.user.icon!;
  }

  selectIcon(path: string): void {
    this.selectedIcon = path;
  }
}
