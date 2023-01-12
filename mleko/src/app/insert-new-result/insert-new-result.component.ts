import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {MlekoService} from "src/app/service/mleko.service";
import {Player, Result} from "src/app/model/models";
import {Router} from "@angular/router";
import {AppComponent} from "src/app/app.component";

@Component({
  selector: 'app-insert-new-result',
  templateUrl: './insert-new-result.component.html',
  styleUrls: ['./insert-new-result.component.css']
})
export class InsertNewResultComponent implements OnInit {

  // @ts-ignore
  secret: string;
  players: Array<Player> = [];
  firstPlace: Array<Player> = [];
  secondPlace: Array<Player> = [];
  thirdPlace: Array<Player> = [];
  fourthPlace: Array<Player> = [];

  constructor(
    private mlekoService: MlekoService,
    private route: Router
  ) {
  }

  ngOnInit(): void {
    this.mlekoService.getPlayers()
      .subscribe(players => this.players = players);
    AppComponent.secret.subscribe(secret => this.secret = secret);
  }

  drop(event: CdkDragDrop<Player[], any>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      transferArrayItem(
        event.previousContainer.data,
        event.container.data,
        event.previousIndex,
        event.currentIndex,
      );
    }
  }

  saveResult() {
    const result: Result = {
      reportedBySecret: this.secret,
      results: [
        this.firstPlace.map(place => place.uuid),
        this.secondPlace.map(place => place.uuid),
        this.thirdPlace.map(place => place.uuid),
        this.fourthPlace.map(place => place.uuid),
      ]
    }
    this.mlekoService.saveResult(result).subscribe(() => {
      this.route.navigate(['last-results'], {queryParams: {secret: this.secret}})
    });
  }

  saveButtonDisabled() {
    return !this.secret || (this.firstPlace.length + this.secondPlace.length + this.thirdPlace.length + this.fourthPlace.length < 2)
  }
}
