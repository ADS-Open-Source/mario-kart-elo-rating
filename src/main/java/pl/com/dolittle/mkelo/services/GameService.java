package pl.com.dolittle.mkelo.services;

import pl.com.dolittle.mkelo.mapstruct.dtos.RankingGameDto;

import java.util.List;

public interface GameService {

    List<RankingGameDto> getTopNGames(Integer n);

    void addGame(RankingGameDto gameDto);
}
