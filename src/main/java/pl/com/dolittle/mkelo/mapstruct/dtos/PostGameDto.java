package pl.com.dolittle.mkelo.mapstruct.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.dolittle.mkelo.mapstruct.validation.AddGameValidation;
import pl.com.dolittle.mkelo.mapstruct.views.GenericViews;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PostGameDto {

    @NotBlank(groups = AddGameValidation.class)
    @JsonView(GenericViews.Public.class)
    private String reportedBySecret;

    @NotBlank(groups = AddGameValidation.class)
    @JsonView(GenericViews.Public.class)
    private List<List<String>> results;
}
