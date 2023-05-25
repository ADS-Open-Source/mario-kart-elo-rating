import {Component, Inject} from '@angular/core';
import {ProcessedGame} from "../../models/Game";
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";
import {MlekoService} from "../../services/mleko.service";
import {SecretService} from "../../services/secret.service";
import {MatSnackBar} from "@angular/material/snack-bar";

export interface DeleteGameDialogData {
  index: number,
  game: ProcessedGame
}

@Component({
  selector: 'app-delete-game-dialog',
  templateUrl: './delete-game-dialog.component.html',
  styleUrls: ['./delete-game-dialog.component.css']
})
export class DeleteGameDialogComponent {

  protected isLatestGame: boolean;

  constructor(@Inject(MAT_DIALOG_DATA) public data: DeleteGameDialogData,
              private dialogRef: MatDialogRef<DeleteGameDialogData>,
              private mlekoService: MlekoService,
              private secretService: SecretService,
              private _snackBar: MatSnackBar) {
    this.isLatestGame = data.index === 0;
  }

  deleteLastGame(): void {
    this.mlekoService.deleteLastGame(this.secretService.secret)
      .subscribe({
        next: (response) => {
          console.log(response);
          this._snackBar.open('Game deleted successfully!', 'Close', {duration: 5000})
          this.dialogRef.close();
        },
        error: (error) => {
          console.error(error);
          this._snackBar.open(error.error, 'Close', {duration: 5000});
        }
      })
  }
}
