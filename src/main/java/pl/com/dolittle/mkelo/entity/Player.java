package pl.com.dolittle.mkelo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@Data
public class Player implements Serializable {

    private String secret;
    private String uuid;
    private String name;
    private String email;
    private int elo;
    private transient int preElo;
    private int gamesPlayed;


    public Player(String secret, String uuid, String name, String email) {
        this.secret = secret;
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.elo = 2000;
        this.gamesPlayed = 0;
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }
}
