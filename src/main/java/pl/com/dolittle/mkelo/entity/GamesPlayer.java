package pl.com.dolittle.mkelo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "games_players")
public class GamesPlayer {
    @EmbeddedId
    private GamesPlayerId id;

    @MapsId("gameId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @MapsId("playerUuid")
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "player_uuid", nullable = false)
    private Player player;

    @Column(name = "place", nullable = false)
    private Integer place;

    @Column(name = "previous_elo")
    private Integer preElo;

    @Column(name = "elo")
    private Integer elo;
}