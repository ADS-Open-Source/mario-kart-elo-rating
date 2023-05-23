package pl.com.dolittle.mkelo.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    Page<Game> findAll(Pageable pageable);

    List<Game> findByGamesPlayersPlayer(@NotNull Player player);

    @Query("SELECT g FROM Game g JOIN g.gamesPlayers gp1 JOIN g.gamesPlayers gp2 WHERE gp1.player = :requester AND gp2.player = :opponent")
    List<Game> findGamesByBothPlayers(@Param("requester") Player requester, @Param("opponent") Player opponent);

}