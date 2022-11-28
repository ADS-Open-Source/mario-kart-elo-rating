package pl.com.dolittle.mkelo.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.control.Players;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerShortDto;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
public class PlayerController {

    private final Players players;
    private JavaMailSender emailSender;


    @GetMapping("/api/player")
    public List<Player> getAllSorted() {
        return players.getAllSorted();
    }

    @PostMapping("/api/player")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addPlayer(@RequestBody PlayerShortDto player) {
        var secret = UUID.randomUUID().toString();
        players.addPlayer(new Player(UUID.randomUUID().toString(), player.getName(), player.getEmail()), secret);
        var message = new SimpleMailMessage();
        message.setFrom("noreply@izb-mail.dolittle.com.pl");
        message.setTo(player.getEmail());
        message.setSubject("Your link to mleko");
        message.setText("http://localhost:4200/new-result?secret=" + secret);
        emailSender.send(message);
    }
}
