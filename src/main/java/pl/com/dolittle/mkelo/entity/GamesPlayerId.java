package pl.com.dolittle.mkelo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class GamesPlayerId implements Serializable {
    @Serial
    private static final long serialVersionUID = 5866107858798398002L;
    @Column(name = "game_id", nullable = false)
    private Long gameId;

    @Column(name = "player_uuid", nullable = false)
    private UUID playerUuid;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GamesPlayerId entity = (GamesPlayerId) o;
        return Objects.equals(this.gameId, entity.gameId) &&
                Objects.equals(this.playerUuid, entity.playerUuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gameId, playerUuid);
    }

}