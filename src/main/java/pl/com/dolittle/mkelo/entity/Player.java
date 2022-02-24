package pl.com.dolittle.mkelo.entity;

import java.io.Serializable;
import java.util.Objects;

public class Player implements Serializable {
    public String uuid;
    public String name;
    public int elo;
    public int gamesPlayed;
    public String email;

    public Player(String uuid, String name, String email) {
        this.uuid = uuid;
        this.name = name;
        this.email = email;
        this.elo = 2000;
        this.gamesPlayed = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return uuid.equals(player.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid);
    }
}
