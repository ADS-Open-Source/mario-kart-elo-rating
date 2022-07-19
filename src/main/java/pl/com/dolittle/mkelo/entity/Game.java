package pl.com.dolittle.mkelo.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class Game implements Serializable {
    public LocalDateTime reportedTime;
    public Player reportedBy;
    public List<List<Result>> result;

    public Game(LocalDateTime reportedTime, Player reportedBy, List<List<Result>> result) {
        this.reportedTime = reportedTime;
        this.reportedBy = reportedBy;
        this.result = result;
    }
    public Game() {
    }
}
