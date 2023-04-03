package pl.com.dolittle.mkelo.mapstruct.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import pl.com.dolittle.mkelo.mapstruct.views.GameViews;

/**
 * A DTO for the {@link pl.com.dolittle.mkelo.entity.GamesPlayer} entity
 */
@Data
public class GamesPlayerDto {

    @JsonView(GameViews.GameHistory.class)
    private PlayerDto playerDto;

    @JsonView(GameViews.GameHistory.class)
    private final Integer place;

    @JsonView(GameViews.GameHistory.class)
    private final Integer previousElo;

    @JsonView(GameViews.GameHistory.class)
    private final Integer elo;
}