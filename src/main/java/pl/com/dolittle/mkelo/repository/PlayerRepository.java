package pl.com.dolittle.mkelo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.Nullable;
import pl.com.dolittle.mkelo.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlayerRepository extends JpaRepository<Player, UUID> {

    List<Player> findByActivatedTrueOrderByEloDesc();

    Optional<Player> findByNameOrEmail(@Nullable String name, @Nullable String email);

    Optional<Player> getBySecret(UUID secret);

    Optional<Player> getByEmail(String email);

    Optional<Player> getByName(String name);
}
