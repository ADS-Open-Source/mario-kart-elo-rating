package pl.com.dolittle.mkelo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.mapstruct.dtos.GameDto;
import pl.com.dolittle.mkelo.mapstruct.dtos.PostGameDto;
import pl.com.dolittle.mkelo.mapstruct.validation.AddGameValidation;
import pl.com.dolittle.mkelo.mapstruct.views.GenericViews;
import pl.com.dolittle.mkelo.repository.GameRepository;
import pl.com.dolittle.mkelo.services.GameService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin({"http://mleko.dolittle.com.pl", "http://mleko.deloitte.cyou"})
@AllArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final GameRepository gameRepository;
    private final GameService gameService;

    @JsonView(GenericViews.Public.class)
    @GetMapping
    public List<GameDto> get(@RequestParam(required = false) Integer count) {
        return gameService.getGames(count);
    }

    @PostMapping
    public ResponseEntity<String> addGame(@RequestBody @Validated(AddGameValidation.class) PostGameDto game) {
        gameRepository.addGame(LocalDateTime.now(), game.getReportedBySecret(), game.getResults());
        return new ResponseEntity<>("game added", HttpStatus.OK);
    }
}
