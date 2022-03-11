import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import {AppComponent} from './app.component';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatIconModule} from "@angular/material/icon";
import {RouterModule, Routes} from "@angular/router";
import { LastResultsComponent } from './last-results/last-results.component';
import { InsertNewResultComponent } from './insert-new-result/insert-new-result.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {MatButtonModule} from "@angular/material/button";

const appRoutes: Routes = [
  { path: 'last-results', component: LastResultsComponent },
  { path: 'new-result', component: InsertNewResultComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    LastResultsComponent,
    InsertNewResultComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule.forRoot(
      appRoutes,
      {enableTracing: true} // <-- debugging purposes only
    ),
    MatToolbarModule,
    MatIconModule,
    DragDropModule,
    MatButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
