import {Component, OnInit} from '@angular/core';
import {MlekoService} from "../service/mleko.service";
import {Game} from "../model/Game";

export interface ProcessedGame {
  date: string;
  result: string;
  reportedBy: string;
}

@Component({
  selector: 'app-last-results',
  templateUrl: './last-results.component.html',
  styleUrls: ['./last-results.component.css']
})
export class LastResultsComponent implements OnInit {

  constructor(private mlekoService: MlekoService) {
  }

  dataSource: ProcessedGame[] = [
    {date: "2022-03-10T19:19:15.9821672+01:00", result: "1. Arti (2067 +11), 2. Marek (1933 -11)", reportedBy: "Arti"},
    {date: "2022-03-10T19:18:56.5413434+01:00", result: "1. Arti (2056 +12), 2. Marek (1944 -12)", reportedBy: "Marek"}
  ];

  displayedColumns: string[] = ['date', 'result', 'reportedBy'];

  ngOnInit(): void {
    // this.games = this.mlekoService.getGames(10);
  }

}
