package pl.com.dolittle.mkelo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "games")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.DETACH)
    @JoinColumn(name = "reported_by", nullable = false)
    private Player reportedBy;

    @Column(name = "reported_time", nullable = false)
    private LocalDateTime reportedTime;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private Set<GamesPlayer> gamesPlayers = new LinkedHashSet<>();

}