package pl.com.dolittle.mkelo.control;

import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.stereotype.Component;
import pl.com.dolittle.mkelo.entity.Player;

import java.io.Serializable;
import java.util.*;

@Component
public class Players implements Serializable {
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
}
