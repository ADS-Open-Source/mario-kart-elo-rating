import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MlekoService} from "../services/mleko.service";
import {Game, ProcessedGame} from "../models/Game";
import {MatTableDataSource} from "@angular/material/table";
import {ScreenSizeService} from "../services/screen-size-service.service";
import {MatDialog} from "@angular/material/dialog";
import {SecretService} from "../services/secret.service";
import {DeleteGameDialogComponent} from "./delete-game-dialog/delete-game-dialog.component";

@Component({
  selector: 'app-last-results',
  templateUrl: './last-results.component.html',
  styleUrls: ['./last-results.component.css']
})
export class LastResultsComponent implements OnInit {

  // TODO dynamically adjust that
  displayedColumns: string[] = ['date', 'result'];
  dataSource!: MatTableDataSource<ProcessedGame>;

  private isActivated: boolean = false;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  constructor(
    public deleteGameDialog: MatDialog,
    private mlekoService: MlekoService,
    private secretService: SecretService,
    protected screenService: ScreenSizeService,
  ) {
  }

  ngOnInit(): void {
    this.displayedColumns = this.screenService.isDesktop ? ['date', 'result', 'reportedBy'] : ['date', 'result'];
    this.mlekoService.getGames(2147483647).subscribe((games: Game[]) => {
      let processedGames = Game.processGames(games)
      this.dataSource = new MatTableDataSource<ProcessedGame>(processedGames);
      this.dataSource.paginator = this.paginator;
      this.secretService.checkIfActivated();
      this.secretService.$isActivatedStore
        .subscribe((isActivated: boolean) => {
          this.isActivated = isActivated;
        })
    });
  }

  openDeleteGameDialog(index: number, game: ProcessedGame): void {
    if (this.isActivated) {
      this.deleteGameDialog.open(DeleteGameDialogComponent, {
        data: {
          index: index,
          game: game
        }
      })
    }
  }
}
