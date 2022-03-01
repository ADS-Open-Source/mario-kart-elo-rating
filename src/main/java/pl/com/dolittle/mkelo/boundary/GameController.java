package pl.com.dolittle.mkelo.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import pl.com.dolittle.mkelo.control.Games;

import java.time.ZonedDateTime;

@RestController
public class GameController {

    private final Games games;

    public GameController(Games games) {
        this.games = games;
    }

    @PostMapping("/game")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void add(@RequestBody GameShort game) {
        games.addGame(ZonedDateTime.now(), game.reportedBySecret, game.results);
    }
}
