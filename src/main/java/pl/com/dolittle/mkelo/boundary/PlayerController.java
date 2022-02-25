package pl.com.dolittle.mkelo.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.control.Players;

import java.util.List;
import java.util.UUID;

@RestController
public class PlayerController {
    private final Players players;

    public PlayerController(Players players) {
        this.players = players;
    }

    @GetMapping("/player")
    public List<Player> getAllSorted() {
        return players.getAllSorted();
    }

    @PostMapping("/player")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addPlayer(@RequestBody PlayerShort player) {
        players.addPlayer(new Player(UUID.randomUUID().toString(), player.name, player.email),
                UUID.randomUUID().toString());
    }
}
