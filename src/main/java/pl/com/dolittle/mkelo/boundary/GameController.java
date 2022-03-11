package pl.com.dolittle.mkelo.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.control.Games;
import pl.com.dolittle.mkelo.entity.Game;

import java.time.ZonedDateTime;
import java.util.List;

@RestController
public class GameController {

    private final Games games;

    public GameController(Games games) {
        this.games = games;
    }

    @PostMapping("/api/game")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void add(@RequestBody GameShort game) {
        games.addGame(ZonedDateTime.now(), game.reportedBySecret, game.results);
    }

    @GetMapping("/api/game")
    public List<Game> get(@RequestParam(required = false) Integer count) {
        return games.getGames(count);
    }
}
