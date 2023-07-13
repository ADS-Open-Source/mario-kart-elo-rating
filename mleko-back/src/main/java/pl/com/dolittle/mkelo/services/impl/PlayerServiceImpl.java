package pl.com.dolittle.mkelo.services.impl;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.exception.*;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;
import pl.com.dolittle.mkelo.mapstruct.mapper.PlayerMapper;
import pl.com.dolittle.mkelo.repository.PlayerRepository;
import pl.com.dolittle.mkelo.services.EmailService;
import pl.com.dolittle.mkelo.services.PersistenceService;
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
    private final PersistenceService persistenceService;

    @Override
    public List<PlayerDto> getAll() {
        return playerMapper.toDtoList(playerRepository.findAll());
    }

    @Override
    public List<PlayerDto> getActivatedSorted() {

        List<Player> players = playerRepository.findByActivatedTrueOrderByEloDesc();
        return playerMapper.toDtoList(players);
    }

    @Override
    public String createPlayer(PlayerDto playerDto, HttpServletRequest request) {

        if (playerRepository.findByNameOrEmail(playerDto.getName(), playerDto.getEmail()).isPresent()) {
            throw new PlayerAlreadyExistsException(playerDto.getName(), playerDto.getEmail());
        }


        Player player = Player.fromPlayerDTO(playerDto);
        Player savedPlayer = playerRepository.save(player);

        String origin = request.getHeader("referer");
        log.info("handling create player request from {}", origin);
        emailService.send(savedPlayer.getEmail(), "Your mkelo secret", savedPlayer.getSecret().toString());

        persistenceService.uploadInsertsData();

        return "email sent to " + savedPlayer.getEmail();
    }

    @Override
    public String activatePlayer(PlayerDto playerDto) {

        UUID secret = UUID.fromString(playerDto.getSecret());
        Player player = playerRepository.getBySecret(secret).orElseThrow(() -> new PlayerSecretNotFoundException(playerDto.getSecret()));

        player.setActivated(true);
        playerRepository.save(player);
        return "player activated";
    }

    @Override
    public Boolean checkIfActivated(UUID secret) {

        Optional<Player> player = playerRepository.getBySecret(UUID.fromString(secret.toString()));
        if (player.isPresent() && Boolean.TRUE.equals(player.get().getActivated())) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override
    public PlayerDto getPlayer(UUID secret) {

        Player player = playerRepository.getBySecret(secret)
                .orElseThrow(() -> new PlayerSecretNotFoundException(secret.toString()));
        return playerMapper.toDto(player);
    }

    @Override
    public Boolean resendSecret(UUID secret, PlayerDto playerDto, HttpServletRequest request) {

        Player requester = playerRepository.getBySecret(secret)
                .orElseThrow(() -> new PlayerSecretNotFoundException(secret.toString()));
        log.info("player {} requested to resend secret: {} {}", requester.getId(), playerDto.getEmail(), playerDto.getName());

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
        playerRepository.save(requester);

        //  find a player
        Player player;
        if (!playerDto.getEmail().isBlank()) {
            player = playerRepository.getByEmail(playerDto.getEmail()).orElseThrow(() -> new PlayerEmailNotFoundException(playerDto.getEmail()));
        } else {
            player = playerRepository.getByName(playerDto.getName()).orElseThrow(() -> new PlayerNameNotFoundException(playerDto.getName()));
        }

        String origin = request.getHeader("referer");
        log.info("handling secret re-sent request from {}", origin);
        emailService.send(player.getEmail(), "Your re-sent mkelo secret", player.getSecret().toString());
        return true;
    }

    @Override
    public PlayerDto changeIcon(UUID secret, PlayerDto playerDto) {
        Player user = playerRepository.getBySecret(secret)
                .orElseThrow(() -> new PlayerSecretNotFoundException(secret.toString()));
        log.info("user {} requested to change icon to: {}", user.getId(), playerDto.getIcon());

        user.setIcon(playerDto.getIcon());
        playerRepository.save(user);
        return playerMapper.toDto(user);
    }
}
