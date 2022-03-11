package pl.com.dolittle.mkelo.boundary;

import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import pl.com.dolittle.mkelo.control.Players;
import pl.com.dolittle.mkelo.entity.Player;

import java.util.List;
import java.util.UUID;

@RestController
public class PlayerController {
    private final Players players;
    private JavaMailSender emailSender;

    public PlayerController(Players players, JavaMailSender emailSender) {
        this.players = players;
        this.emailSender = emailSender;
    }

    @GetMapping("/api/player")
    public List<Player> getAllSorted() {
        return players.getAllSorted();
    }

    @PostMapping("/api/player")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void addPlayer(@RequestBody PlayerShort player) {
        var secret = UUID.randomUUID().toString();
        players.addPlayer(new Player(UUID.randomUUID().toString(), player.name, player.email), secret);
        var message = new SimpleMailMessage();
        message.setFrom("noreply@izb-mail.dolittle.com.pl");
        message.setTo(player.email);
        message.setSubject("Your link to mleko");
        message.setText("http://mleko.dolittle.com.pl/new-result?secret=" + secret);
        emailSender.send(message);
    }
}
