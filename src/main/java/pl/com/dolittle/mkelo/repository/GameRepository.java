package pl.com.dolittle.mkelo.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import pl.com.dolittle.mkelo.control.third.ELOMatch;
import pl.com.dolittle.mkelo.control.third.ELOPlayer;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.entity.Result;
import pl.com.dolittle.mkelo.exception.AuthenticationFailedException;
import pl.com.dolittle.mkelo.exception.PlayerUUIDNotFoundException;
import pl.com.dolittle.mkelo.services.FileService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
@AllArgsConstructor
public class GameRepository {

    private FileService fileService;
    private PlayerRepository playerRepository;


    public void addGame(LocalDateTime reportedTime, String reportedBySecret, List<List<String>> ranking) {

        List<Game> games = fileService.getGamesDataFromS3();
        Optional<Player> reportedBy = playerRepository.getBySecret(reportedBySecret);
        if (reportedBy.isEmpty()) {
            log.error("Player with a secret {} doesn't exist", reportedBySecret);
            throw new AuthenticationFailedException(reportedBySecret);
        }
        var rankingPlayers = ranking.stream()
                .map(l -> l.stream()
                        .map(uuid -> playerRepository.getById(uuid).orElseThrow(() -> new PlayerUUIDNotFoundException(uuid)))
                        .toList())
                .toList();
        ELOMatch match = new ELOMatch();
        for (int i = 0; i < rankingPlayers.size(); i++) {
            for (var player : rankingPlayers.get(i)) {
                match.addPlayer(new ELOPlayer(player.getUuid(), i + 1, player.getElo()));
            }
        }
        match.calculateELOs();
        log.info("match info: {}", match);

        var rankingResult = rankingPlayers.stream()
                .map(l -> l.stream().map(p -> {
                            var oldElo = p.getElo();
                            p.setElo(match.getELO(p.getUuid()));
                            p.incrementGamesPlayed();
                            return new Result(p, oldElo, p.getElo());
                        })
                        .toList())
                .toList();

        updatePlayersData(match);
        games.add(0, new Game(reportedTime, reportedBy.orElseThrow(() -> new AuthenticationFailedException(reportedBySecret)), rankingResult));
        fileService.putGamesDataToS3(games);
    }

    public List<Game> getGames(Integer count) {
        List<Game> games = fileService.getGamesDataFromS3();
        if (Objects.isNull(count) || count <= 0) {
            return Collections.unmodifiableList(games);
        } else {
            return Collections.unmodifiableList(games.subList(0, Math.min(count, games.size())));
        }
    }

    private void updatePlayersData(ELOMatch match) {
        List<Player> playersData = fileService.getPlayersDataFromS3();
        playersData.forEach(v -> {
            if (match.checkIfIsPlayer(v.getUuid())) {
                v.setElo(match.getELO(v.getUuid()));
                v.incrementGamesPlayed();
            }
        });
        fileService.putPlayersDataToS3(playersData);
    }
}
