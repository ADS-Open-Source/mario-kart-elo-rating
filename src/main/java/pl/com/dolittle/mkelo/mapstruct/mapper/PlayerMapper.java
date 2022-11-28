package pl.com.dolittle.mkelo.mapstruct.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PlayerMapper {

    //  DTO to Entity
    Player toEntity(PlayerDto playerDto);

    // Entity to DTO
    PlayerDto toDto(Player player);

    List<PlayerDto> toDtoList(List<Player> players);
}
