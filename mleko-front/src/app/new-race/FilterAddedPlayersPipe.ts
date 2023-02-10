import {Pipe, PipeTransform} from '@angular/core';
import {Player} from "../models/Player";

@Pipe({
  name: 'filterAddedPlayers'
})
export class FilterAddedPlayersPipe implements PipeTransform {

  transform(players: Player[], allAddedPlayers: Player[][]): Player[] {
    const addedPlayers = allAddedPlayers.flat();
    return players.filter(player => !addedPlayers.some(addedPlayer => addedPlayer === player));
  }
}
