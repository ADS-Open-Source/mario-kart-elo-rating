package pl.com.dolittle.mkelo.entity;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class Game {
    public ZonedDateTime reportedTime;
    public Player reportedBy;
    public List<Set<Player>> result;
}
