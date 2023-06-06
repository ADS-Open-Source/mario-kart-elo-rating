package pl.com.dolittle.mkelo.mapstruct.dtos;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.dolittle.mkelo.mapstruct.validation.CreatePlayerValidation;
import pl.com.dolittle.mkelo.mapstruct.validation.ResendEmailValidation;
import pl.com.dolittle.mkelo.mapstruct.validation.SecretValidation;
import pl.com.dolittle.mkelo.mapstruct.views.GameViews;
import pl.com.dolittle.mkelo.mapstruct.views.GenericViews;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PlayerDto {

    @NotBlank(groups = SecretValidation.class)
    @JsonView(GenericViews.Private.class)
    private String secret;

    @JsonView({GenericViews.Public.class, GameViews.GameHistory.class})
    private UUID uuid;

    @NotBlank(groups = CreatePlayerValidation.class)
    @NotNull(groups = ResendEmailValidation.class)
    @JsonView({GenericViews.Public.class, GameViews.GameHistory.class})
    private String name;

    @NotBlank(groups = CreatePlayerValidation.class)
    @NotNull(groups = ResendEmailValidation.class)
    @JsonView(GenericViews.Private.class)
    private String email;

    @JsonView(GenericViews.Private.class)
    private boolean activated;

    @JsonView(GenericViews.Public.class)
    private int elo;

    @JsonView(GameViews.GameHistory.class)
    private int preElo;

    @JsonView(GameViews.GameHistory.class)
    private int place;

    @JsonView(GenericViews.Public.class)
    private int gamesPlayed;

    @JsonView(GenericViews.Public.class)
    private String icon;
}
