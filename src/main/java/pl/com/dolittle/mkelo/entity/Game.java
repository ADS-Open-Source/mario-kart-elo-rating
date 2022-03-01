package pl.com.dolittle.mkelo.entity;

import java.time.ZonedDateTime;
import java.util.List;

public class Game {
    public ZonedDateTime reportedTime;
    public Player reportedBy;
    public List<List<Result>> result;

    public Game(ZonedDateTime reportedTime, Player reportedBy, List<List<Result>> result) {
        this.reportedTime = reportedTime;
        this.reportedBy = reportedBy;
        this.result = result;
    }
}
