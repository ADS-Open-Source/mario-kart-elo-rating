package pl.com.dolittle.mkelo.mapstruct.dtos;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.dolittle.mkelo.mapstruct.validation.CreatePlayerValidation;
import pl.com.dolittle.mkelo.mapstruct.views.GenericViews;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
public class PlayerDto {

    @JsonView(GenericViews.Private.class)
    private String uuid;

    @NotBlank(groups = CreatePlayerValidation.class)
    @JsonView(GenericViews.Public.class)
    private String name;

    @NotBlank(groups = CreatePlayerValidation.class)
    @JsonView(GenericViews.Public.class)
    private String email;

    @JsonView(GenericViews.Private.class)
    private int elo;

    @JsonView(GenericViews.Private.class)
    private int gamesPlayed;
}
