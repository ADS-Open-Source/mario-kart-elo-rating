package pl.com.dolittle.mkelo.mapstruct.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.mapstruct.views.GenericViews;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MKEloDataDto {

    @JsonView(GenericViews.Private.class)
    private String filename;

    @JsonView(GenericViews.Public.class)
    private List<Player> players;

    @JsonView(GenericViews.Public.class)
    private List<Game> games;
}
