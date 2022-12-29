package pl.com.dolittle.mkelo.mapstruct.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.mapstruct.views.GenericViews;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GameDto {

    @JsonView(GenericViews.Public.class)
    private LocalDateTime reportedTime;

    @JsonView(GenericViews.Public.class)
    private Player reportedBy;

    @JsonView(GenericViews.Public.class)
    private List<List<String>> results;
}
