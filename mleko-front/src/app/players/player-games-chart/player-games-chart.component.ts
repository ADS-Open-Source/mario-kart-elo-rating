import {Component, Input, OnInit} from '@angular/core';
import {Game} from "../../models/Game";
import {ScreenSizeService} from "../../services/screen-size-service.service";

export interface ChartGame {
  name: string,
  series: Array<{
    name: string | Date,
    value: number,
    min: number
    max: number
  }>
}

@Component({
  selector: 'app-player-games-chart',
  templateUrl: './player-games-chart.component.html',
  styleUrls: ['./player-games-chart.component.css']
})
export class PlayerGamesChartComponent implements OnInit {

  @Input() games: Array<Game> | null = null;
  @Input() username!: string;
  chartData: ChartGame[] | null = null;

  // options
  legend: boolean = false;
  showLabels: boolean = true;
  animations: boolean = true;
  xAxis: boolean = true;
  yAxis: boolean = true;
  minYAxisValue: number = 1900;
  maxYAxisValue: number = 2100;
  showXAxisLabel: boolean = true;
  showYAxisLabel: boolean = true
  xAxisLabel: string = 'Time';
  yAxisLabel: string = 'Elo';
  timeline: boolean = true;

  constructor(
    private screenService: ScreenSizeService,
  ) {
  }

  ngOnInit() {
    this.showYAxisLabel = this.screenService.isDesktop
    if (this.games) {
      this.chartData = this.processChartData(this.games, this.username);
    }
  }

  private groupByDate(games: Game[], username: string): { [key: string]: { gameDate: Date, elo: number }[] } {
    const groupedData: { [key: string]: { gameDate: Date, elo: number }[] } = {};
    const userGames = games.map(game => {
      return {
        gameDate: new Date(game.reportedTime),
        elo: game.ranking.flatMap(r => r).find(r => r.name === username)?.elo!
      }
    })
    userGames.forEach(userGame => {
      const date: string = userGame.gameDate.toLocaleDateString();
      if (!groupedData[date]) {
        groupedData[date] = [];
      }
      groupedData[date].push(userGame);
    });

    return groupedData;
  }

  private processChartData(games: Game[], username: string): ChartGame[] {
    const groupedData = this.groupByDate(games, username);
    const seriesData = Object.keys(groupedData).map((date) => {
      const sortedScores = groupedData[date].sort((a, b) => a.gameDate.getTime() - b.gameDate.getTime());
      const eloValues = groupedData[date].map(r => r.elo);
      this.maxYAxisValue = Math.max(this.maxYAxisValue, ...eloValues);  // scale the Y-axis
      this.minYAxisValue = Math.min(this.minYAxisValue, ...eloValues);
      return {
        name: new Date (date),
        value: sortedScores[sortedScores.length - 1].elo,
        min: Math.min(...eloValues),
        max: Math.max(...eloValues)
      }
    })
    return [{name: username, series: seriesData}]
  }

  onSelect(data: Event): void {
    console.log('Item clicked', JSON.parse(JSON.stringify(data)));
  }

  onActivate(data: Event): void {
    console.log('Activate', JSON.parse(JSON.stringify(data)));
  }

  onDeactivate(data: Event): void {
    console.log('Deactivate', JSON.parse(JSON.stringify(data)));
  }
}
