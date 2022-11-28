package pl.com.dolittle.mkelo.mapstruct.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GameShortDto {

    private String reportedBySecret;
    private List<List<String>> results;
}
