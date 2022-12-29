package pl.com.dolittle.mkelo.mapstruct.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.mapstruct.dtos.GameDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GameMapper {

    //  DTO to Entity
    Game toEntity(GameDto gameDto);

    // Entity to DTO
    GameDto toDto(Game game);

    List<GameDto> toDtoList(List<Game> games);
}
