package pl.com.dolittle.mkelo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.control.Games;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.mapstruct.dtos.GameShortDto;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/game")
public class GameController {

    private final Games games;

    @GetMapping
    public List<Game> get(@RequestParam(required = false) Integer count) {
        return games.getGames(count);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void add(@RequestBody GameShortDto game) {
        games.addGame(LocalDateTime.now(), game.getReportedBySecret(), game.getResults());
    }
}
