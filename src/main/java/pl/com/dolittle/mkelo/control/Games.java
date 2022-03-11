package pl.com.dolittle.mkelo.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import pl.com.dolittle.mkelo.control.third.ELOMatch;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Result;
import pl.com.dolittle.mkelo.util.AuthenticationFailedException;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
public class Games {
    private static final Logger LOGGER = LoggerFactory.getLogger(Games.class);

    private final LinkedList<Game> games = new LinkedList<>();

    private final Players players;

    public Games(Players players) {
        this.players = players;
    }

    public void addGame(ZonedDateTime reportedTime, String reportedBySecret, List<List<String>> ranking) {
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
    }

    public List<Game> getGames(Integer count) {
        if (Objects.isNull(count) || count <= 0) {
            return Collections.unmodifiableList(games);
        } else {
            return Collections.unmodifiableList(games.subList(0, Math.min(count, games.size())));
        }
    }
}
