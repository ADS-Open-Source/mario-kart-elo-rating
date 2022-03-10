package pl.com.dolittle.mkelo.control;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.com.dolittle.mkelo.control.third.ELOMatch;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.entity.Result;
import pl.com.dolittle.mkelo.util.AuthenticationFailedException;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class Data implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Data.class);
    private final LinkedList<Game> games = new LinkedList<>();
    private Set<Player> players = new HashSet<>();
    private Map<String, Player> secrets = new HashMap<>();

    public void addPlayer(Player player, String secret) {
        Validate.notNull(player.name);
        Validate.isTrue(players.stream().noneMatch(i -> Objects.equals(i.name, player.name)),
                "Player with the given name already exists");
        Validate.isTrue(players.stream().noneMatch(i -> Objects.equals(i.email, player.email)),
                "Player with the given email already exists");
        players.add(player);
        secrets.put(secret, player);
        LOGGER.info("Player {} has been given secret {}", player.name, secret);
    }

    public List<Player> getAllSorted() {
        List<Player> result = new ArrayList<>(players);
        result.sort((o1, o2) -> new CompareToBuilder()
                .append(o2.elo, o1.elo)
                .append(o1.gamesPlayed, o2.gamesPlayed)
                .append(o1.name, o2.name)
                .toComparison());
        return result;
    }

    public Optional<Player> getBySecret(String secret) {
        return Optional.of(secrets.get(secret));
    }

    public Optional<Player> getById(String uuid) {
        return players.stream().filter(p -> Objects.equals(uuid, p.uuid)).findAny();
    }

    public void addGame(ZonedDateTime reportedTime, String reportedBySecret, List<List<String>> ranking) {
        var reportedBy = getBySecret(reportedBySecret);
        if (reportedBy.isEmpty()) {
            throw new AuthenticationFailedException();
        }
        var rankingPlayers = ranking.stream()
                .map(l -> l.stream().map(uuid -> getById(uuid).orElseThrow()).collect(Collectors.toList()))
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
