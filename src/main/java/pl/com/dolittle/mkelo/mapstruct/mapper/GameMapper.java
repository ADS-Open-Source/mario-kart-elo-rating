package pl.com.dolittle.mkelo.mapstruct.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.mapstruct.dtos.GameDto;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GameMapper {

    // DTO to Entity

    // Entity to DTO
    @Mapping(target = "reportedBySecret", source = "reportedBy.uuid")  // TODO ask whether uuid == secret
    @Mapping(target = "results", source = "result")
    GameDto toDto(Game game);
}
