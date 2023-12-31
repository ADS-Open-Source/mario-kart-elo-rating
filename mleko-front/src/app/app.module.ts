import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';

import {MatTableModule} from "@angular/material/table";
import {AppComponent} from './app.component';
import {NavbarComponent} from './navbar/navbar.component';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatButtonModule} from "@angular/material/button";
import {MatIconModule} from "@angular/material/icon";
import {HttpClientModule} from "@angular/common/http";
import {RouterModule, Routes} from "@angular/router";
import {HomepageComponent} from './homepage/homepage.component';
import {NewRaceComponent} from './new-race/new-race.component';
import {RankingComponent} from './ranking/ranking.component';
import {LastResultsComponent} from './last-results/last-results.component';
import {DatePipe, NgOptimizedImage} from "@angular/common";
import {MAT_DATE_LOCALE} from "@angular/material/core";
import {MatPaginatorModule} from "@angular/material/paginator";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {MatListModule} from "@angular/material/list";
import {MatInputModule} from "@angular/material/input";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {PlayersComponent} from './players/players.component';
import {MatSnackBarModule} from "@angular/material/snack-bar";
import {MatMenuModule} from "@angular/material/menu";
import {NgxMatSelectSearchModule} from "ngx-mat-select-search";
import {GamesDialogComponent} from './ranking/games-dialog/games-dialog.component';
import {MatDialogModule} from "@angular/material/dialog";
import { DeleteGameDialogComponent } from './last-results/delete-game-dialog/delete-game-dialog.component';
import { ChangeIconDialogComponent } from './players/change-icon-dialog/change-icon-dialog.component';
import {MatTooltipModule} from "@angular/material/tooltip";
import { PlayerGamesChartComponent } from './players/player-games-chart/player-games-chart.component';
import {LineChartModule, NgxChartsModule} from "@swimlane/ngx-charts";


const appRoutes: Routes = [

  {path: 'new-race', component: NewRaceComponent},
  {path: 'ranking', component: RankingComponent},
  {path: 'last-races', component: LastResultsComponent},
  {path: 'players', component: PlayersComponent},
  {path: '', component: HomepageComponent},
  {path: '**', redirectTo: ''}
]

@NgModule({
  declarations: [
    //  components
    AppComponent,
    NavbarComponent,
    HomepageComponent,
    NewRaceComponent,
    RankingComponent,
    LastResultsComponent,
    //  others
    PlayersComponent,
    PlayersComponent,
    GamesDialogComponent,
    DeleteGameDialogComponent,
    ChangeIconDialogComponent,
    PlayerGamesChartComponent
  ],
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatTableModule,
        MatListModule,
        MatPaginatorModule,
        MatToolbarModule,
        MatButtonModule,
        MatIconModule,
        RouterModule.forRoot(
            appRoutes,
        ),
        MatFormFieldModule,
        MatSelectModule,
        MatSnackBarModule,
        MatInputModule,
        MatMenuModule,
        ReactiveFormsModule,
        MatSlideToggleModule,
        FormsModule,
        NgxMatSelectSearchModule,
        MatDialogModule,
        MatTooltipModule,
        LineChartModule,
        NgxChartsModule,
        NgOptimizedImage,
    ],
  providers: [
    DatePipe,
    {provide: MAT_DATE_LOCALE, useValue: 'pl-PL'}
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
}
