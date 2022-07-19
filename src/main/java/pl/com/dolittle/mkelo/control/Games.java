package pl.com.dolittle.mkelo.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pl.com.dolittle.mkelo.control.third.ELOMatch;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Result;
import pl.com.dolittle.mkelo.services.FileService;
import pl.com.dolittle.mkelo.util.AuthenticationFailedException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class Games {
    private static final Logger LOGGER = LoggerFactory.getLogger(Games.class);

    @Autowired
    private FileService fileService;
    private LinkedList<Game> games = new LinkedList<>();

    private final Players players;

    public Games(Players players) {
        this.players = players;
    }

    public void addGame(LocalDateTime reportedTime, String reportedBySecret, List<List<String>> ranking) {

        games = fileService.getGamesDataFromS3();
        var reportedBy = players.getBySecret(reportedBySecret);
        if (reportedBy.isEmpty()) {
            throw new AuthenticationFailedException();
        }
        var rankingPlayers = ranking.stream()
                .map(l -> l.stream().map(uuid -> players.getById(uuid).orElseThrow()).collect(Collectors.toList()))
                .collect(Collectors.toList());
        var match = new ELOMatch();
        for (int i = 0; i < rankingPlayers.size(); i++) {
            for (var player : rankingPlayers.get(i)) {
                match.addPlayer(player.uuid, i+1, player.elo);
            }
        }
        match.calculateELOs();
        LOGGER.info(match.toString());

        var rankingResult = rankingPlayers.stream()
                .map(l -> l.stream().map(p -> {
                            var oldElo = p.elo;
                            p.elo = match.getELO(p.uuid);
                            p.gamesPlayed++;
                            return new Result(p, oldElo, p.elo);
                        })
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());

        games.addFirst(new Game(reportedTime, reportedBy.orElseThrow(), rankingResult));
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
}
