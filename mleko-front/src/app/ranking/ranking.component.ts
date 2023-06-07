import {Component, OnDestroy, OnInit} from '@angular/core';
import {Player} from "../models/Player";
import {MlekoService} from "../services/mleko.service";
import {MatIconRegistry} from "@angular/material/icon";
import {DomSanitizer} from "@angular/platform-browser";
import {ActivatedRoute} from "@angular/router";
import {MatTableDataSource} from "@angular/material/table";
import {Subscription} from "rxjs";
import {ScreenSizeService} from "../services/screen-size-service.service";
import {MatDialog} from "@angular/material/dialog";
import {GamesDialogComponent} from "./games-dialog/games-dialog.component";
import {SecretService} from "../services/secret.service";
import {Game} from "../models/Game";

@Component({
  selector: 'app-ranking',
  templateUrl: './ranking.component.html',
  styleUrls: ['./ranking.component.css']
})
export class RankingComponent implements OnInit, OnDestroy {

  playersSub!: Subscription;
  players: Array<Player> = [];
  displayedColumns: string[] = ['position', 'icon', 'username', 'elo'];
  dataSource: MatTableDataSource<Player> = new MatTableDataSource<Player>();
  showOnlyChads: boolean = true;

  constructor(
    public gamesDialog: MatDialog,
    private mlekoService: MlekoService,
    private secretService: SecretService,
    private matIconRegistry: MatIconRegistry,
    private domSanitizer: DomSanitizer,
    private route: ActivatedRoute,
    protected screenService: ScreenSizeService,
  ) {
    this.matIconRegistry.addSvgIcon(
      'trophy',
      this.domSanitizer.bypassSecurityTrustResourceUrl('assets/trophy.svg')
    );

    this.route.queryParamMap.subscribe(params => {
      if (params.get('hasło') === 'masło') {
        this.displayedColumns = ['position', 'icon', 'username', 'gamesPlayed', 'elo'];
      }
    });
  }

  ngOnInit(): void {
    this.secretService.checkIfActivated();
    this.playersSub = this.mlekoService.$playersStore
      .subscribe((players: Player[]) => {
        this.players = players;
        this.filterOutNewPlayers();
      });
  }

  ngOnDestroy(): void {
    this.playersSub.unsubscribe();
  }

  filterOutNewPlayers(): void {
    this.players = this.players.map(player => ({
      ...player,
        icon: player.icon ?? MlekoService.getSeededImagePath(player, 'assets/player-icons', 72)
    }))
    this.dataSource.data = this.showOnlyChads ? this.players.filter(p => p.gamesPlayed >= 10) : this.players;
  }

  openGameDetailsDialog(player: Player): void {
    if (this.secretService.isActivated) {
      this.mlekoService.getGamesByPlayer(this.secretService.secret, player.name)
        .subscribe({
          next: (res: Game[]) => {
            this.gamesDialog.open(GamesDialogComponent, {
              data: {
                player: player,
                games: Game.processGames(res)
              }
            })
          }
        });
    }
  }
}
