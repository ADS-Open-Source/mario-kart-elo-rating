package pl.com.dolittle.mkelo.entity;

public class Result {
    public Player player;
    public int eloBefore, eloAfter;

    public Result(Player player, int eloBefore, int eloAfter) {
        this.player = player;
        this.eloBefore = eloBefore;
        this.eloAfter = eloAfter;
    }
}
