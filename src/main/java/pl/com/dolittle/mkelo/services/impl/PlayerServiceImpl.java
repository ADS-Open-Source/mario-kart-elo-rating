package pl.com.dolittle.mkelo.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.exception.*;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;
import pl.com.dolittle.mkelo.mapstruct.mapper.PlayerMapper;
import pl.com.dolittle.mkelo.repository.PlayerRepository;
import pl.com.dolittle.mkelo.services.EmailService;
import pl.com.dolittle.mkelo.services.PlayerService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final EmailService emailService;

    @Override
    public List<PlayerDto> getActivatedSorted() {

        List<Player> players = playerRepository.getAllSorted();
        return playerMapper.toDtoList(players.stream().filter(Player::isActivated).toList());
    }

    @Override
    public String createPlayer(PlayerDto playerDto) {

        if (playerDto.getName().isBlank() || playerDto.getEmail().isBlank()) {
            throw new InvalidPlayerCreationDataProvidedException();
        }
        var secret = UUID.randomUUID().toString();
        playerRepository.addPlayer(new Player(secret, UUID.randomUUID().toString(), playerDto.getName(), playerDto.getEmail()));

        String messageContent = "http://mleko.dolittle.com.pl/new-result?secret=" + secret;
        emailService.send(playerDto.getEmail(), "Your link to mleko", messageContent);
        return "email sent to " + playerDto.getEmail();
    }

    @Override
    public String activatePlayer(PlayerDto playerDto) {

        String secret = playerDto.getSecret();

        playerRepository.activatePlayer(secret);
        return "player activated";
    }

    @Override
    public Boolean checkIfActivated(String secret) {

        Optional<Player> player = playerRepository.getBySecret(secret);
        if (player.isPresent() && player.get().isActivated()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean resendSecret(String secret, PlayerDto playerDto) {

        //  check if requester exists TODO add JavaDocs rather than comments
        Player requester = playerRepository.getBySecret(secret).orElseThrow(() -> new PlayerSecretNotFoundException(secret));
        log.info("player {} requested to resend secret: {} {}", requester.getUuid(), playerDto.getEmail(), playerDto.getName());

        //  check whether requester can send additional requests
        LocalDateTime requestTime = LocalDateTime.now();
        if (requester.getLastEmailRequest() != null) {
            Duration resendCooldown = Duration.between(requester.getLastEmailRequest(), requestTime);
            if (resendCooldown.toDays() <= 1) {
                throw new TooManyResendRequestsException(requestTime, requester.getLastEmailRequest().plusDays(1));
            }
        }

        // update the last email request time
        requester.setLastEmailRequest(requestTime);
        playerRepository.updatePlayers();

        //  find a player
        Player player;
        if (!playerDto.getEmail().isBlank()) {
            player = playerRepository.getByEmail(playerDto.getEmail()).orElseThrow(() -> new PlayerEmailNotFoundException(playerDto.getEmail()));
        } else {
            player = playerRepository.getByName(playerDto.getName()).orElseThrow(() -> new PlayerNameNotFoundException(playerDto.getName()));
        }

        String messageContent = "http://mleko.dolittle.com.pl/new-result?secret=" + player.getSecret();
        emailService.send(player.getEmail(), "Your re-sent link to mleko", messageContent);
        return true;
    }
}
