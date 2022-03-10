package pl.com.dolittle.mkelo.entity;

import java.io.Serializable;

public class Result implements Serializable {
    public Player player;
    public int eloBefore, eloAfter;

    public Result(Player player, int eloBefore, int eloAfter) {
        this.player = player;
        this.eloBefore = eloBefore;
        this.eloAfter = eloAfter;
    }
}
