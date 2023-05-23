import {Component, OnInit, ViewChild} from '@angular/core';
import {MatPaginator} from "@angular/material/paginator";
import {MlekoService} from "../services/mleko.service";
import {Game, ProcessedGame} from "../models/Game";
import {MatTableDataSource} from "@angular/material/table";
import {ScreenSizeService} from "../services/screen-size-service.service";

@Component({
  selector: 'app-last-results',
  templateUrl: './last-results.component.html',
  styleUrls: ['./last-results.component.css']
})
export class LastResultsComponent implements OnInit {

  // TODO dynamically adjust that
  displayedColumns: string[] = this.screenService.isDesktop ? ['date', 'result', 'reportedBy'] : ['date', 'result'];
  dataSource!: MatTableDataSource<ProcessedGame>;

  @ViewChild(MatPaginator, {static: true}) paginator!: MatPaginator;

  constructor(
    private mlekoService: MlekoService,
    protected screenService: ScreenSizeService,
  ) {
  }

  ngOnInit(): void {
    this.mlekoService.getGames(2147483647).subscribe((games: Game[]) => {
      let processedGames = Game.processGames(games)
      this.dataSource = new MatTableDataSource<ProcessedGame>(processedGames);
      this.dataSource.paginator = this.paginator;
    });
  }

}
