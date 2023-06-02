package pl.com.dolittle.mkelo.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.entity.*;
import pl.com.dolittle.mkelo.exception.PlayerNameNotFoundException;
import pl.com.dolittle.mkelo.exception.PlayerSecretNotFoundException;
import pl.com.dolittle.mkelo.exception.PlayerUUIDNotFoundException;
import pl.com.dolittle.mkelo.exception.UnauthorisedAction;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;
import pl.com.dolittle.mkelo.mapstruct.dtos.RankingGameDto;
import pl.com.dolittle.mkelo.mapstruct.mapper.GameMapper;
import pl.com.dolittle.mkelo.repository.GameRepository;
import pl.com.dolittle.mkelo.repository.PlayerRepository;
import pl.com.dolittle.mkelo.services.GameService;
import pl.com.dolittle.mkelo.services.PersistenceService;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final PlayerRepository playerRepository;
    private final PersistenceService persistenceService;

    @Override
    public List<RankingGameDto> getTopNGames(Integer n) {

        Page<Game> page = gameRepository.findAll(PageRequest.of(0, n, Sort.by(Sort.Order.desc("reportedTime"))));
        return RankingGameDto.fromGameDtoList(gameMapper.toDtoList(page.getContent()));
    }

    @Override
    public List<RankingGameDto> getGamesBySecret(UUID secret) {

        //  find a player
        Player player = playerRepository.getBySecret(secret)
                .orElseThrow(() -> new PlayerSecretNotFoundException(secret.toString()));

        //  find games
        List<Game> games = gameRepository.findByGamesPlayersPlayer(player);
        return RankingGameDto.fromGameDtoList(gameMapper.toDtoList(games));
    }

    @Override
    public List<RankingGameDto> getGamesWithOpponent(UUID requesterSecret, String opponentName) {

        //  find a requester
        Player requester = playerRepository.getBySecret(requesterSecret)
                .orElseThrow(() -> new PlayerSecretNotFoundException(requesterSecret.toString()));

        //  find the opponent
        Player opponent = playerRepository.getByName(opponentName)
                .orElseThrow(() -> new PlayerNameNotFoundException(opponentName));
        //  find games between requester and opponent
        List<Game> games = gameRepository.findGamesByBothPlayers(requester, opponent);

        return RankingGameDto.fromGameDtoList(gameMapper.toDtoList(games));
    }


    @Override
    public void addGame(RankingGameDto gameDto) {
        // added as a way to circumvent stupid mapstruct behaviour
        Game game = new Game();
        game.setReportedTime(LocalDateTime.now());  // set time

        UUID reportedBySecret = UUID.fromString(gameDto.getReportedBy().getSecret());
        Player reportedBy = playerRepository.getBySecret(reportedBySecret)
                .orElseThrow(() -> new PlayerSecretNotFoundException(reportedBySecret.toString()));
        game.setReportedBy(reportedBy);  // set reportedBy

        // calculate eloChange
        ELOMatch match = new ELOMatch();
        List<List<PlayerDto>> ranking = gameDto.getRanking();
        for (int i = 0; i < ranking.size(); i++) {
            for (PlayerDto playerDto : ranking.get(i)) {
                Player player = playerRepository.findById(playerDto.getUuid())
                        .orElseThrow(() -> new PlayerUUIDNotFoundException(playerDto.getUuid().toString()));
                player.setPlace(i + 1);
                player.incrementGamesPlayed();
                player.setPreElo(player.getElo());
                match.addPlayer(player);
            }
        }

        match.calculateELOs();
        log.info("match info: {}", match);

        Set<GamesPlayer> gamesPlayers = new HashSet<>();
        for (Player player : match.getPlayers()) {
            GamesPlayer gamesPlayer = new GamesPlayer(
                    new GamesPlayerId(game.getId(), player.getId()),
                    game, player, player.getPlace(), player.getPreElo(), player.getElo()
            );
            gamesPlayers.add(gamesPlayer);
        }
        game.setGamesPlayers(gamesPlayers);
        gameRepository.save(game);

        persistenceService.uploadInsertsDataToS3();
    }


    @Override
    public void deleteLast(UUID requesterSecret) {
        // get latest game
        Game game = gameRepository.findMostRecentGame();
        // check if requester matches the sender
        if (!game.getReportedBy().getSecret().equals(requesterSecret)) {
            throw new UnauthorisedAction();
        }

        for (GamesPlayer gp : game.getGamesPlayers()) {
            Player player = gp.getPlayer();
            player.decrementGamesPlayed();
            player.setElo(gp.getPreElo());
            playerRepository.save(player);
        }

        gameRepository.delete(game);

        persistenceService.uploadInsertsDataToS3();
    }
}
