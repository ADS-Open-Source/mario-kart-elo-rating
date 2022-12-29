package pl.com.dolittle.mkelo.services;

import pl.com.dolittle.mkelo.mapstruct.dtos.GameDto;

import java.util.List;

public interface GameService {

    List<GameDto> getGames(Integer count);
}
