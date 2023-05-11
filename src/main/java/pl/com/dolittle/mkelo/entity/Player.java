package pl.com.dolittle.mkelo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.com.dolittle.mkelo.exception.InvalidPlayerCreationDataProvidedException;
import pl.com.dolittle.mkelo.mapstruct.dtos.PlayerDto;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID id;

    @Column(name = "secret", nullable = false)
    private UUID secret;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
    private String email;

    @Column(name = "activated")
    private Boolean activated = false;

    @Column(name = "elo")
    private Integer elo = 2000;

    @Column(name = "gamesplayed")
    private Integer gamesPlayed = 0;

    @Column(name = "lastemailrequest")
    private LocalDateTime lastEmailRequest = LocalDateTime.now();

    @Transient
    private int preElo;

    @Transient
    private int place;

    public Player(String name, String email) {
        this.id = UUID.randomUUID();
        this.secret = UUID.randomUUID();
        this.name = name;
        this.email = email;
    }

    public void addToElo(int addend) {
        this.elo += addend;
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }

    public static Player fromPlayerDTO(PlayerDto playerDto) {
        // RFC 5322 email standard regex validator
        if (playerDto.getName().isBlank() || !Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
                .matcher(playerDto.getEmail()).matches()) {
            throw new InvalidPlayerCreationDataProvidedException();
        }
        return new Player(playerDto.getName(), playerDto.getEmail());
    }
}