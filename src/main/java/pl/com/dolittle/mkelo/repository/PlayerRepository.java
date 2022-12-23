package pl.com.dolittle.mkelo.repository;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.services.FileService;

import java.io.Serializable;
import java.util.*;

@Repository
@Slf4j
public class PlayerRepository implements Serializable {

    @Autowired
    private FileService fileService;
    private Map<String, Player> secrets = new HashMap<>();
    private Set<Player> players = new HashSet<>(secrets.values());


    public void addPlayer(Player player, String secret) {
        secrets = fileService.getPlayersDataFromS3();
        players = new HashSet<>(secrets.values());
        Validate.notNull(player.getName());
        Validate.isTrue(players.stream().noneMatch(i -> Objects.equals(i.getName(), player.getName())),
                "Player with the given name already exists");
        Validate.isTrue(players.stream().noneMatch(i -> Objects.equals(i.getEmail(), player.getEmail())),
                "Player with the given email already exists");
        players.add(player);
        secrets.put(secret, player);
        log.info("Player {} has been given secret {}", player.getName(), secret);
        fileService.putPlayersDataToS3(secrets);
    }

    public List<Player> getAllSorted() {
        secrets = fileService.getPlayersDataFromS3();
        players = new HashSet<>(secrets.values());
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
        secrets = fileService.getPlayersDataFromS3();
        return Optional.ofNullable(secrets.get(secret));
    }

    public Optional<Player> getById(String uuid) {
        log.info("Looking for a player with uuid {}", uuid);
        secrets = fileService.getPlayersDataFromS3();
        players = new HashSet<>(secrets.values());
        return players.stream().filter(p -> Objects.equals(uuid, p.getUuid())).findAny();
    }
}

