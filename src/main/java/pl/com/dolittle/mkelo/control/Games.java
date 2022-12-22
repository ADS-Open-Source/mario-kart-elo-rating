package pl.com.dolittle.mkelo.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.com.dolittle.mkelo.control.third.ELOMatch;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.entity.Result;
import pl.com.dolittle.mkelo.exception.AuthenticationFailedException;
import pl.com.dolittle.mkelo.services.FileService;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class Games {
    private static final Logger LOGGER = LoggerFactory.getLogger(Games.class);

    @Autowired
    private FileService fileService;
    private List<Game> games = new LinkedList<>();

    private final Players players;

    public Games(Players players) {
        this.players = players;
    }

    public void addGame(LocalDateTime reportedTime, String reportedBySecret, List<List<String>> ranking) {

        games = fileService.getGamesDataFromS3();
        Player reportedBy = players.getBySecret(reportedBySecret).orElseThrow(() -> new AuthenticationFailedException(reportedBySecret));
        var rankingPlayers = ranking.stream()
                .map(l -> l.stream().map(uuid -> players.getById(uuid).orElseThrow()).toList())
                .toList();
        var match = new ELOMatch();
        for (int i = 0; i < rankingPlayers.size(); i++) {
            for (var player : rankingPlayers.get(i)) {
                match.addPlayer(player.getUuid(), i+1, player.getElo());
            }
        }
        match.calculateELOs();
        LOGGER.info("match info: {}", match);

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
        games.add(0, new Game(reportedTime, reportedBy, rankingResult));
        fileService.putGamesDataToS3(games);
    }

    public List<Game> getGames(Integer count) {
        games = fileService.getGamesDataFromS3();
        if (Objects.isNull(count) || count <= 0) {
            return Collections.unmodifiableList(games);
        } else {
            return Collections.unmodifiableList(games.subList(0, Math.min(count, games.size())));
        }
    }

    private void updatePlayersData(ELOMatch match){
        Map<String, Player> playersData = fileService.getPlayersDataFromS3();
        playersData.forEach((k, v) -> { if(match.checkIfIsPlayer(v.getUuid())){v.setElo(match.getELO(v.getUuid()));
        v.incrementGamesPlayed();}
        });
        fileService.putPlayersDataToS3(playersData);
    }
}
