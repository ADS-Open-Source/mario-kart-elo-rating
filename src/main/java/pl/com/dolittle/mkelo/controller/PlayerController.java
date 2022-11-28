package pl.com.dolittle.mkelo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;
import pl.com.dolittle.mkelo.mapstruct.validation.CreatePlayerValidation;
import pl.com.dolittle.mkelo.mapstruct.views.GenericViews;
import pl.com.dolittle.mkelo.services.PlayerService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {

    private PlayerService playerService;


    @JsonView(GenericViews.Private.class)
    @GetMapping("/all")
    public List<PlayerDto> getAllSorted() {
        return playerService.getAllSorted();
    }

    @JsonView(GenericViews.Public.class)
    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public String addPlayer(@RequestBody @Validated(CreatePlayerValidation.class) PlayerDto player) {
        return playerService.createPlayer(player);
    }
}
