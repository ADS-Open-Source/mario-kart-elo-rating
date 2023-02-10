import {Component, OnInit} from '@angular/core';
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import {ActivatedRoute} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";

@Component({
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  styleUrls: ['./ranking.component.css']
})
export class RankingComponent implements OnInit {

  players: Array<Player> = [];
  displayedColumns: string[] = ['position', 'username', 'elo'];
  dataSource: MatTableDataSource<Player> = new MatTableDataSource<Player>();
  showOnlyChads: boolean = true;

  constructor(
    private mlekoService: MlekoService,
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private route: ActivatedRoute,
  ) {
    this.matIconRegistry.addSvgIcon(
      'trophy',
      this.domSanitizer.bypassSecurityTrustResourceUrl('assets/trophy.svg')
    );

    this.route.queryParamMap.subscribe(params => {
      if (params.get('hasło') === 'masło') {
        this.displayedColumns = ['position', 'username', 'gamesPlayed', 'elo'];
      }
    });
  }

  ngOnInit(): void {
    this.mlekoService.getPlayers()
      .subscribe((players: Player[]) => {
        this.players = players;
        this.filterOutNewPlayers();
      });
  }

  filterOutNewPlayers(): void {
    this.dataSource.data = this.showOnlyChads ? this.players.filter(p => p.gamesPlayed >= 10) : this.players;
  }
}
