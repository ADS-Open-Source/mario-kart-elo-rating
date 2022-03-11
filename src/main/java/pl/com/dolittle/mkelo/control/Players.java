package pl.com.dolittle.mkelo.control;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import pl.com.dolittle.mkelo.entity.Player;

import java.io.Serializable;
import java.util.*;

@Component
public class Players implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(Players.class);
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
}
