import { Component, OnInit } from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {MlekoService} from "src/app/service/mleko.service";
import {Player, Result} from "src/app/model/models";

@Component({
  selector: 'app-insert-new-result',
  templateUrl: './insert-new-result.component.html',
  styleUrls: ['./insert-new-result.component.css']
})
export class InsertNewResultComponent implements OnInit {

  players: Array<Player> = [];
  firstPlace: Array<Player> = [];
  secondPlace: Array<Player> = [];
  thirdPlace: Array<Player> = [];
  fourthPlace: Array<Player> = [];

  constructor(
    private mlekoService: MlekoService
  ) { }

  ngOnInit(): void {
    this.mlekoService.getPlayers()
      .subscribe(players => this.players = players);
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
      reportedBySecret: '',
      results: [
        this.firstPlace.map(place => place.uuid),
        this.secondPlace.map(place => place.uuid),
        this.thirdPlace.map(place => place.uuid),
        this.fourthPlace.map(place => place.uuid),
      ]
    }
    this.mlekoService.saveResult(result).subscribe(()=> window.alert('Saved'));
  }
}
