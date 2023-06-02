package pl.com.dolittle.mkelo.controller;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;
import pl.com.dolittle.mkelo.mapstruct.validation.CreatePlayerValidation;
import pl.com.dolittle.mkelo.mapstruct.validation.ResendEmailValidation;
import pl.com.dolittle.mkelo.mapstruct.validation.SecretValidation;
import pl.com.dolittle.mkelo.mapstruct.views.GenericViews;
import pl.com.dolittle.mkelo.services.PlayerService;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin({"http://mleko.dolittle.com.pl", "http://mleko.deloitte.cyou"})
@AllArgsConstructor
@RequestMapping("/api/players")
public class PlayerController {

    private PlayerService playerService;


    @JsonView(GenericViews.Public.class)
    @GetMapping("/all")
    public List<PlayerDto> getAllSorted() {
        return playerService.getActivatedSorted();
    }

    @JsonView(GenericViews.Public.class)
    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public String addPlayer(@RequestBody @Validated(CreatePlayerValidation.class) PlayerDto playerDto) {
        return playerService.createPlayer(playerDto);
    }

    @GetMapping("/activated/{secret}")
    public ResponseEntity<Boolean> checkIfActivated(@PathVariable UUID secret) {
        return new ResponseEntity<>(playerService.checkIfActivated(secret), HttpStatus.OK);
    }

    @JsonView(GenericViews.Private.class)
    @GetMapping("/whoami/{secret}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable UUID secret) {
        return new ResponseEntity<>(playerService.getPlayer(secret), HttpStatus.OK);
    }

    @PostMapping("/activate")
    public String activatePlayer(@RequestBody @Validated(SecretValidation.class) PlayerDto playerDto) {
        return playerService.activatePlayer(playerDto);
    }

    @PostMapping("/resend/{requesterSecret}")
    public ResponseEntity<Boolean> resendSecret(@RequestBody @Validated(ResendEmailValidation.class) PlayerDto playerDto,
                                                @PathVariable UUID requesterSecret) {
        return new ResponseEntity<>(playerService.resendSecret(requesterSecret, playerDto), HttpStatus.OK);
    }
}
