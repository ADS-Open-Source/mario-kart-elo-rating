package pl.com.dolittle.mkelo.mapstruct.mapper;

import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.mapstruct.dtos.GameDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {PlayerMapper.class, GamesPlayerMapper.class})
public interface GameMapper {

    @Named("gameToGameDto")
    GameDto toDto(Game game);

    @IterableMapping(qualifiedByName = "gameToGameDto")
    List<GameDto> toDtoList(List<Game> games);
}
