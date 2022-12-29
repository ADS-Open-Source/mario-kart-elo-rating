package pl.com.dolittle.mkelo.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.repository.PlayerRepository;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;
import pl.com.dolittle.mkelo.mapstruct.mapper.PlayerMapper;
import pl.com.dolittle.mkelo.services.EmailService;
import pl.com.dolittle.mkelo.services.PlayerService;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final PlayerRepository playerRepository;
    private final PlayerMapper playerMapper;
    private final EmailService emailService;

    @Override
    public List<PlayerDto> getAllSorted() {
        return playerMapper.toDtoList(playerRepository.getAllSorted());
    }

    @Override
    public String createPlayer(PlayerDto playerDto) {

        var secret = UUID.randomUUID().toString();
        playerRepository.addPlayer(new Player(secret, UUID.randomUUID().toString(), playerDto.getName(), playerDto.getEmail()));

        String messageContent = "http://mleko.dolittle.com.pl/new-result?secret=" + secret;
        emailService.send(playerDto.getEmail(), "Your link to mleko", messageContent);
        return "email sent to " + playerDto.getEmail();
    }

}
