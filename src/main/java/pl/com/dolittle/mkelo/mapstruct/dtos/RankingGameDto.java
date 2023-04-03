package pl.com.dolittle.mkelo.mapstruct.dtos;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.dolittle.mkelo.mapstruct.views.GameViews;
import pl.com.dolittle.mkelo.mapstruct.views.GenericViews;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankingGameDto {

    @JsonView(GenericViews.Public.class)
    private LocalDateTime reportedTime;

    @JsonView(GenericViews.Public.class)
    private PlayerDto reportedBy;

    @JsonView(GameViews.GameHistory.class)
    private List<List<PlayerDto>> ranking;


    public static RankingGameDto fromGameDto(GameDto gameDto) {

        List<PlayerDto> playerDtos = new ArrayList<>();
        for (GamesPlayerDto gamesPlayerDto : gameDto.getGamesPlayers()) {
            playerDtos.add(gamesPlayerDto.getPlayerDto());
        }

        return new RankingGameDto(gameDto.getReportedTime(), gameDto.getReportedBy(), Collections.singletonList(playerDtos));
    }

    public static List<RankingGameDto> fromGameDtoList(List<GameDto> gameDtoList) {
        List<RankingGameDto> rankingGameDtos = new ArrayList<>();
        for (GameDto gameDto : gameDtoList) {
            rankingGameDtos.add(RankingGameDto.fromGameDto(gameDto));
        }
        return rankingGameDtos;
    }
}

