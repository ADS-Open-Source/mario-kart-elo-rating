import {Component, OnInit} from '@angular/core';
import {CdkDragDrop, moveItemInArray, transferArrayItem} from "@angular/cdk/drag-drop";
import {MlekoService} from "src/app/service/mleko.service";
import {Player, PlayerSecret, Result} from "src/app/model/models";
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
  isActivated: boolean = true;
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
      .subscribe((players: Player[]) => this.players = players);

    AppComponent.secret.subscribe((secret: string) => this.secret = secret);

    if (this.secret != null) {
      this.mlekoService.isActivated(this.secret)
        .subscribe((isActivated: boolean) => this.isActivated = isActivated);
    }
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
      reportedBy: {
        secret: this.secret
      },
      ranking: [
        this.firstPlace,
        this.secondPlace,
        this.thirdPlace,
        this.fourthPlace,
      ]
    }
    this.mlekoService.saveResult(result).subscribe((res: { text: string; }) => {
      console.log(res)
      this.route.navigate(['last-results'], {queryParams: {secret: this.secret}})
    });
  }

  saveButtonDisabled() {
    return !this.secret || (this.firstPlace.length + this.secondPlace.length + this.thirdPlace.length + this.fourthPlace.length < 2)
  }

  activatePlayer() {
    const playerSecret: PlayerSecret = {
      secret: this.secret
    }
    this.mlekoService.activatePlayer(playerSecret).subscribe();
  }

  activateButtonDisabled() {
    return !this.secret;
  }
}
