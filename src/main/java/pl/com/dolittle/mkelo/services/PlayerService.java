package pl.com.dolittle.mkelo.services;

import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;

import java.util.List;
import java.util.UUID;

public interface PlayerService {

    List<PlayerDto> getAll();

    List<PlayerDto> getActivatedSorted();

    String createPlayer(PlayerDto playerDto);

    String activatePlayer(PlayerDto playerDto);

    Boolean checkIfActivated(UUID secret);

    PlayerDto getPlayer(UUID secret);

    Boolean resendSecret(UUID secret, PlayerDto playerDto);
}
