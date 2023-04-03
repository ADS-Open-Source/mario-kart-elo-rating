package pl.com.dolittle.mkelo.mapstruct.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.dolittle.mkelo.mapstruct.views.GameViews;
import pl.com.dolittle.mkelo.mapstruct.views.GenericViews;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class GameDto {

    @JsonView(GenericViews.Public.class)
    private LocalDateTime reportedTime;

    @JsonView(GenericViews.Public.class)
    private PlayerDto reportedBy;

    @JsonView(GameViews.GameHistory.class)
    private Set<GamesPlayerDto> gamesPlayers;
}
