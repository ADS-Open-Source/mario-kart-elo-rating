package pl.com.dolittle.mkelo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;


@NoArgsConstructor
@Data
public class Player implements Serializable {

    private String secret;
    private String uuid;
    private String name;
    private String email;
    private boolean activated;
    private int elo;
    private transient int preElo;
    private transient int place;
    private int gamesPlayed;
    private LocalDateTime lastEmailRequest;


    public Player(String secret, String uuid, String name, String email) {
        this.secret = secret;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.activated = false;
        this.elo = 2000;
        this.gamesPlayed = 0;
        this.lastEmailRequest = LocalDateTime.now();
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }

    public void addToElo(float number) {
        this.elo += number;
    }

    public void activate() {
        this.activated = true;
    }
}
