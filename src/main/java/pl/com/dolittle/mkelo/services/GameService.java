package pl.com.dolittle.mkelo.services;

import pl.com.dolittle.mkelo.mapstruct.dtos.RankingGameDto;

import java.util.List;

public interface GameService {

    List<RankingGameDto> getTopNGames(Integer n);

    List<RankingGameDto> getGamesBySecret(String secret);
    List<RankingGameDto> getGamesWithOpponent(String secret, String opponentName);

    void addGame(RankingGameDto gameDto);
}
