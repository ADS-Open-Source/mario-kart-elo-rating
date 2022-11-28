package pl.com.dolittle.mkelo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@NoArgsConstructor
@Data
public class Player implements Serializable {

    private String uuid;
    private String name;
    private String email;
    private int elo;
    private int gamesPlayed;


    public Player(String uuid, String name, String email) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.elo = 2000;
        this.gamesPlayed = 0;
    }

    public void incrementGamesPlayed() {
        this.gamesPlayed++;
    }

//  TODO purge that
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Player player = (Player) o;
//        return uuid.equals(player.uuid);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(uuid);
//    }
}
