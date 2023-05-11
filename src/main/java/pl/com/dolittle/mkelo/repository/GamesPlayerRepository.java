package pl.com.dolittle.mkelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.com.dolittle.mkelo.entity.GamesPlayer;
import pl.com.dolittle.mkelo.entity.GamesPlayerId;

public interface GamesPlayerRepository extends JpaRepository<GamesPlayer, GamesPlayerId> {
}