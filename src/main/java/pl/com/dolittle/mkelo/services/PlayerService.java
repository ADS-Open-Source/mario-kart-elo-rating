package pl.com.dolittle.mkelo.services;

import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;

import java.util.List;

public interface PlayerService {

    List<PlayerDto> getActivatedSorted();

    String createPlayer(PlayerDto playerDto);
}
