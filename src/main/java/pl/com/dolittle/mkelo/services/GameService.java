package pl.com.dolittle.mkelo.services;

import pl.com.dolittle.mkelo.mapstruct.dtos.RankingGameDto;

import java.util.List;
import java.util.Optional;

public interface GameService {

    List<RankingGameDto> getTopNGames(Integer n);

    List<RankingGameDto> getGames(String secret, Optional<String> opponentName);

    void addGame(RankingGameDto gameDto);
}
