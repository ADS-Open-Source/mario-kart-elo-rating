package pl.com.dolittle.mkelo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.mapstruct.dtos.RankingGameDto;
import pl.com.dolittle.mkelo.mapstruct.validation.SecretValidation;
import pl.com.dolittle.mkelo.mapstruct.views.GameViews;
import pl.com.dolittle.mkelo.services.GameService;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin({"http://mleko.dolittle.com.pl", "http://mleko.deloitte.cyou"})
@AllArgsConstructor
@RequestMapping("/api/games")
public class GameController {

    private final GameService gameService;

    @JsonView(GameViews.GameHistory.class)
    @GetMapping
    public List<RankingGameDto> get(@RequestParam(required = false) Integer count) {
        return gameService.getTopNGames(count);
    }

    @JsonView(GameViews.GameHistory.class)
    @GetMapping("/{requesterSecret}")
    public ResponseEntity<List<RankingGameDto>> getGames(@PathVariable String requesterSecret,
                                                         @RequestParam(name = "opponent") Optional<String> opponentName) {
        // TODO create two separate service functions whether opponentName is present or not
        return new ResponseEntity<>(gameService.getGames(requesterSecret, opponentName), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> addGame(@RequestBody @Validated(SecretValidation.class) RankingGameDto gameDto) {
        gameService.addGame(gameDto);
        return new ResponseEntity<>("game added", HttpStatus.OK);
    }
}
