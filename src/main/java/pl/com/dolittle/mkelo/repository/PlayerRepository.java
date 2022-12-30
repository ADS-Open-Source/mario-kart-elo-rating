package pl.com.dolittle.mkelo.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.exception.PlayerSecretNotFoundException;
import pl.com.dolittle.mkelo.services.DataService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@Slf4j
public class PlayerRepository {

    @Autowired
    private DataService dataService;
    private List<Player> players = new ArrayList<>();


    public void addPlayer(Player player) {
        players = dataService.getPlayersData();
        Validate.notNull(player.getName());
        Validate.isTrue(players.stream().noneMatch(i -> Objects.equals(i.getName(), player.getName())),
                "Player with the given name already exists");
        Validate.isTrue(players.stream().noneMatch(i -> Objects.equals(i.getEmail(), player.getEmail())),
                "Player with the given email already exists");
        players.add(player);
        log.info("Player {} has been given secret {}", player.getName(), player.getSecret());
        dataService.putPlayersData(players);
    }

    public List<Player> getAllSorted() {
        players = dataService.getPlayersData();
        List<Player> result = new ArrayList<>(players);
        result.sort((o1, o2) -> new CompareToBuilder()
                .append(o2.getElo(), o1.getElo())
                .append(o1.getGamesPlayed(), o2.getGamesPlayed())
                .append(o1.getName(), o2.getName())
                .toComparison());
        return result;
    }

    public Optional<Player> getBySecret(String secret) {
        log.info("Looking for a player with secret {}", secret);
        players = dataService.getPlayersData();
        return players.stream().filter(p -> Objects.equals(secret, p.getSecret())).findAny();
    }

    public Optional<Player> getById(String uuid) {
        log.info("Looking for a player with uuid {}", uuid);
        players = dataService.getPlayersData();
        return players.stream().filter(p -> Objects.equals(uuid, p.getUuid())).findAny();
    }

    public void activatePlayer(String secret) {
        Player player = this.getBySecret(secret).orElseThrow(() -> new PlayerSecretNotFoundException(secret));
        log.info("Activating player with secret: {}", secret);
        player.activate();
        dataService.putPlayersData(players);
    }
}

