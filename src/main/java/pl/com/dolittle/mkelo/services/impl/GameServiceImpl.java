package pl.com.dolittle.mkelo.services.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.mapstruct.dtos.GameDto;
import pl.com.dolittle.mkelo.mapstruct.mapper.GameMapper;
import pl.com.dolittle.mkelo.repository.GameRepository;
import pl.com.dolittle.mkelo.services.GameService;

import java.util.List;

@Service
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public List<GameDto> getGames(Integer count) {
        return gameMapper.toDtoList(gameRepository.getGames(count));
    }
}
