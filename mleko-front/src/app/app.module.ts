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
import {DatePipe} from "@angular/common";
import {MAT_DATE_LOCALE} from "@angular/material/core";
import {MatPaginatorModule} from "@angular/material/paginator";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatSelectModule} from "@angular/material/select";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {FilterAddedPlayersPipe} from "./new-race/FilterAddedPlayersPipe";
import {MatListModule} from "@angular/material/list";
import {MatInputModule} from "@angular/material/input";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import { PlayersComponent } from './players/players.component';
import {MatSnackBarModule} from "@angular/material/snack-bar";


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
    FilterAddedPlayersPipe,
    PlayersComponent
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
    ReactiveFormsModule,
    MatSlideToggleModule,
    FormsModule,
  ],
  providers: [
    DatePipe,
    {provide: MAT_DATE_LOCALE, useValue: 'pl-PL'}
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
}
