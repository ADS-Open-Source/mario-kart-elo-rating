package pl.com.dolittle.mkelo.control;

import org.springframework.stereotype.Controller;
import pl.com.dolittle.mkelo.entity.Game;

import java.time.ZonedDateTime;
import java.util.LinkedList;
import java.util.List;

@Controller
public class Games {
    private final LinkedList<Game> games = new LinkedList<>();

    public void addGame(ZonedDateTime zonedDateTime, String addedBySecret, List<List<String>> ranking) {

    }
}
