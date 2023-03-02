package pl.com.dolittle.mkelo.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.exception.InvalidPlayerCreationDataProvidedException;
import pl.com.dolittle.mkelo.exception.PlayerEmailNotFoundException;
import pl.com.dolittle.mkelo.exception.PlayerSecretNotFoundException;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;
import pl.com.dolittle.mkelo.mapstruct.mapper.PlayerMapper;
import pl.com.dolittle.mkelo.repository.PlayerRepository;
import pl.com.dolittle.mkelo.services.EmailService;
import pl.com.dolittle.mkelo.services.PlayerService;

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
    public Boolean resendSecret(String secret, String email) {

        playerRepository.getBySecret(secret).orElseThrow(() -> new PlayerSecretNotFoundException(secret));

        Player player = playerRepository.getByEmail(email).orElseThrow(() -> new PlayerEmailNotFoundException(email));

        String messageContent = "http://mleko.dolittle.com.pl/new-result?secret=" + player.getSecret();
        emailService.send(player.getEmail(), "Your re-sent link to mleko", messageContent);
        return true;
    }
}
