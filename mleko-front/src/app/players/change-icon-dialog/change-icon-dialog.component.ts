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

  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }
}
