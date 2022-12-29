package pl.com.dolittle.mkelo.services.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;
import pl.com.dolittle.mkelo.mapstruct.dtos.GameDto;
import pl.com.dolittle.mkelo.mapstruct.mapper.GameMapper;
import pl.com.dolittle.mkelo.mapstruct.mapper.PlayerMapper;
import pl.com.dolittle.mkelo.repository.GameRepository;
import pl.com.dolittle.mkelo.services.GameService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final PlayerMapper playerMapper;

    @Override
    public List<GameDto> getGames(Integer count) {
        return gameMapper.toDtoList(gameRepository.getGames(count));
    }

    @Override
    public void addGame(GameDto gameDto) {
        // added as a way to circumvent stupid mapstruct behaviour
        Game game = new Game();
        game.setReportedTime(LocalDateTime.now());
        game.setReportedBy(playerMapper.toEntity(gameDto.getReportedBy()));
        List<List<Player>> ranking = gameDto.getRanking()
                .stream().map(playerDtos -> playerDtos.stream().map(playerMapper::toEntity).toList())
                .toList();
        game.setRanking(ranking);

        gameRepository.addGame(game);
    }
}
