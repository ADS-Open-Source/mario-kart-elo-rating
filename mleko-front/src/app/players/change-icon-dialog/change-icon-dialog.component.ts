import {Component, Inject} from '@angular/core';
import {Player} from "../../models/Player";
import {MAT_DIALOG_DATA} from "@angular/material/dialog";
import {MlekoService} from "../../services/mleko.service";
import {SecretService} from "../../services/secret.service";
import {MatSnackBar} from "@angular/material/snack-bar";

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
  isProcessing: boolean = false;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: DialogData,
    private mlekoService: MlekoService,
    private secretService: SecretService,
    private _snackBar: MatSnackBar,
  ) {
    this.iconsPaths = Array(72).fill(1).map((x, i) => `assets/player-icons/${i + 1}.png`)
    this.selectedIcon = data.user.icon!;
  }

  selectIcon(iconPath: string): void {
    this.selectedIcon = iconPath;
  }

  changeIcon(iconPath: string) {
    this.isProcessing = true;
    this.mlekoService.changeUserIcon(this.secretService.secret, iconPath)
      .subscribe({
        next: (player: Player) => {
          console.log(player)
          this.isProcessing = false;
          this._snackBar.open('Icon changed successfully!', 'Close', {duration: 3000})
          this.secretService.updateCurrentUser();
          this.mlekoService.loadPlayers();
        },
        error: (error) => {
          console.error(error)
          this.isProcessing = false;
          this._snackBar.open(error.error, 'Close', {duration: 5000})
        }
      })
  }

}
