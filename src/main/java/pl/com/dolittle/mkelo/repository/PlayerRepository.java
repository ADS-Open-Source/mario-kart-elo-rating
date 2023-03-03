package pl.com.dolittle.mkelo.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.exception.PlayerAlreadyExistsException;
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
        if (players.stream()
                .anyMatch(p -> Objects.equals(p.getName(), player.getName())
                        ||
                        Objects.equals(p.getEmail(), player.getEmail()))) {
            throw new PlayerAlreadyExistsException(player.getName(), player.getEmail());
        }
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

    public Optional<Player> getByEmail(String email) {
        log.info("Looking for a player with email {}", email);
        players = dataService.getPlayersData();
        return players.stream().filter(p -> Objects.equals(email, p.getEmail())).findAny();
    }

    public Optional<Player> getByName(String name) {
        log.info("Looking for a player with name {}", name);
        players = dataService.getPlayersData();
        return players.stream().filter(p -> Objects.equals(name, p.getName())).findAny();
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

