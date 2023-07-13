package pl.com.dolittle.mkelo.mapstruct.mapper;

import org.mapstruct.*;
import pl.com.dolittle.mkelo.entity.GamesPlayer;
import pl.com.dolittle.mkelo.mapstruct.dtos.GamesPlayerDto;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING, uses = {PlayerMapper.class})
public interface GamesPlayerMapper {
    GamesPlayer toEntity(GamesPlayerDto gamesPlayerDto);

    @Mapping(target = "playerDto", source = "player")
    @Mapping(target = "playerDto.place", source = "place")
    @Mapping(target = "playerDto.preElo", source = "preElo")
    @Mapping(target = "playerDto.elo", source = "elo")
    GamesPlayerDto toDto(GamesPlayer gamesPlayer);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    GamesPlayer partialUpdate(GamesPlayerDto gamesPlayerDto, @MappingTarget GamesPlayer gamesPlayer);
}