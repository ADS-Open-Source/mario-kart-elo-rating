package pl.com.dolittle.mkelo.services;

import pl.com.dolittle.mkelo.mapstruct.dtos.RankingGameDto;

import java.util.List;
import java.util.UUID;

public interface GameService {

    List<RankingGameDto> getTopNGames(Integer n);
    List<RankingGameDto> getGamesBySecret(String secret);
    List<RankingGameDto> getGamesWithOpponent(String secret, String opponentName);

    void addGame(RankingGameDto gameDto);

    void deleteLast(UUID requesterSecret);
}
