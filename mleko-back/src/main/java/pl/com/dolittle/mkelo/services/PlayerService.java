package pl.com.dolittle.mkelo.services;

import jakarta.servlet.http.HttpServletRequest;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;

import java.util.List;
import java.util.UUID;

public interface PlayerService {

    List<PlayerDto> getAll();

    List<PlayerDto> getActivatedSorted();

    String createPlayer(PlayerDto playerDto, HttpServletRequest request);

    String activatePlayer(PlayerDto playerDto);

    Boolean checkIfActivated(UUID secret);

    PlayerDto getPlayer(UUID secret);

    Boolean resendSecret(UUID secret, PlayerDto playerDto, HttpServletRequest request);

    PlayerDto changeIcon(UUID secret, PlayerDto playerDto);
}
