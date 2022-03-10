import {Component, OnInit} from '@angular/core';
import {MlekoService} from "../service/mleko.service";
import {Game} from "../model/Game";

@Component({
  selector: 'app-last-results',
  templateUrl: './last-results.component.html',
  styleUrls: ['./last-results.component.css']
})
export class LastResultsComponent implements OnInit {

  constructor(private mlekoService: MlekoService) {
  }

  games: Game[] | undefined;

  ngOnInit(): void {
    this.games = this.mlekoService.getGames(10);
  }

}
