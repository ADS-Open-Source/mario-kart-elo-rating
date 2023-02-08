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


const appRoutes: Routes = [

  {path: 'new-race', component: NewRaceComponent},
  {path: 'ranking', component: RankingComponent},
  {path: 'last-races', component: LastResultsComponent},
  {path: '', component: HomepageComponent},
  {path: '**', redirectTo: ''}
]

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    HomepageComponent,
    NewRaceComponent,
    RankingComponent,
    LastResultsComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MatTableModule,
    MatPaginatorModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    RouterModule.forRoot(
      appRoutes,
    ),
  ],
  providers: [
    DatePipe,
    {provide: MAT_DATE_LOCALE, useValue: 'pl-PL'}
  ],
  bootstrap: [AppComponent],
})
export class AppModule {
}
