package pl.com.dolittle.mkelo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.control.Games;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.mapstruct.dtos.GameDto;
import pl.com.dolittle.mkelo.mapstruct.validation.AddGameValidation;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin({"http://mleko.dolittle.com.pl", "http://mleko.deloitte.cyou"})
@AllArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final Games games;

    @GetMapping
    public List<Game> get(@RequestParam(required = false) Integer count) {
        return games.getGames(count);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addGame(@RequestBody @Validated(AddGameValidation.class) GameDto game) {
        games.addGame(LocalDateTime.now(), game.getReportedBySecret(), game.getResults());
    }
}
