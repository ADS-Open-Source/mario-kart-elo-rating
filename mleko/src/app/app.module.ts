import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from "@angular/material/toolbar";
import { MatIconModule } from "@angular/material/icon";
import { RouterModule, Routes } from "@angular/router";
import { LastResultsComponent } from './last-results/last-results.component';
import { InsertNewResultComponent } from './insert-new-result/insert-new-result.component';
import {DragDropModule} from "@angular/cdk/drag-drop";
import {MatButtonModule} from "@angular/material/button";
import { UserAddComponent } from './user-add/user-add.component';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import {MatTableModule} from "@angular/material/table";

const appRoutes: Routes = [
  { path: 'last-results', component: LastResultsComponent },
  { path: 'user-add', component: UserAddComponent },
  { path: 'new-result', component: InsertNewResultComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    LastResultsComponent,
    UserAddComponent,
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
    MatFormFieldModule,
    MatInputModule,
    MatTableModule,
    MatIconModule,
    DragDropModule,
    MatButtonModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {
}
