package pl.com.dolittle.mkelo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.control.Games;
import pl.com.dolittle.mkelo.entity.Game;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
public class GameController {

    private final Games games;

    @PostMapping("/api/game")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void add(@RequestBody GameShort game) {
        games.addGame(LocalDateTime.now(), game.reportedBySecret, game.results);
    }

    @GetMapping("/api/game")
    public List<Game> get(@RequestParam(required = false) Integer count) {
        return games.getGames(count);
    }
}
