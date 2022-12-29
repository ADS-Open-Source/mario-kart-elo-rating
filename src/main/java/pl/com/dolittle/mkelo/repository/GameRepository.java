package pl.com.dolittle.mkelo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.com.dolittle.mkelo.entity.ELOMatch;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.exception.AuthenticationFailedException;
import pl.com.dolittle.mkelo.exception.PlayerUUIDNotFoundException;
import pl.com.dolittle.mkelo.services.DataService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
public class GameRepository {

    private DataService dataService;
    private PlayerRepository playerRepository;


    public void addGame(Game game) {

        String reportedBySecret = game.getReportedBy().getSecret();
        List<List<Player>> ranking = game.getRanking();
        List<Game> games = dataService.getGamesData();

        Optional<Player> reportedBy = playerRepository.getBySecret(reportedBySecret);
        if (reportedBy.isEmpty()) {
            log.error("Player with a secret {} doesn't exist", reportedBySecret);
            throw new AuthenticationFailedException(reportedBySecret);
        }
        ranking = ranking.stream()
                .map(l -> l.stream()
                        .map(player -> playerRepository.getById(player.getUuid()).orElseThrow(() -> new PlayerUUIDNotFoundException(player.getUuid())))
                        .toList())
                .toList();
        ELOMatch match = new ELOMatch();
        for (int i = 0; i < ranking.size(); i++) {
            for (var player : ranking.get(i)) {
                player.setPlace(i+1);
                player.setPreElo(player.getElo());
                match.addPlayer(player);
            }
        }
        match.calculateELOs();
        log.info("match info: {}", match);

        updatePlayersData(match);
        games.add(0, new Game(game.getReportedTime(), reportedBy.orElseThrow(() -> new AuthenticationFailedException(reportedBySecret)), ranking));
        dataService.putGamesData(games);
    }

    public List<Game> getGames(Integer count) {
        List<Game> games = dataService.getGamesData();
        if (Objects.isNull(count) || count <= 0) {
            return Collections.unmodifiableList(games);
        } else {
            return Collections.unmodifiableList(games.subList(0, Math.min(count, games.size())));
        }
    }

    private void updatePlayersData(ELOMatch match) {
        List<Player> playersData = dataService.getPlayersData();
        playersData.forEach(v -> {
            if (match.checkIfIsPlayer(v.getUuid())) {
                v.setElo(match.getELO(v.getUuid()));
                v.incrementGamesPlayed();
            }
        });
        dataService.putPlayersData(playersData);
    }
}
