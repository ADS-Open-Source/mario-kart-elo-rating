package pl.com.dolittle.mkelo.entity;

import java.time.ZonedDateTime;
import java.util.List;

public class Game {
    public ZonedDateTime reportedTime;
    public Player reportedBy;
    public List<List<Game>> result;
}
