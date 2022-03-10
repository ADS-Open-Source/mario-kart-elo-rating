package pl.com.dolittle.mkelo.control;

import org.springframework.stereotype.Component;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;
import pl.setblack.airomem.core.Persistent;
import pl.setblack.airomem.core.Query;
import pl.setblack.airomem.core.VoidCommand;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.List;

@Component
public class PersistentData {

    private static final Path STORE_FOLDER = Paths.get("./persistentData");

    private Persistent<Data> persistent;

    @PostConstruct
    public void init() {
        persistent = Persistent.loadOptional(STORE_FOLDER, Data::new);
    }

    public void addPlayer(Player player, String secret) {
        persistent.execute(new AddPlayerCommand(player, secret));
    }

    public List<Player> getAllSorted() {
        return persistent.query(new GetAllSortedCommand());
    }

    public void addGame(ZonedDateTime reportedTime, String reportedBySecret, List<List<String>> ranking) {
        persistent.execute(new AddGameCommand(reportedTime, reportedBySecret, ranking));
    }

    public List<Game> getGames(Integer count) {
        return persistent.query(new GetGamesCommand(count));
    }

    private static final class AddGameCommand implements VoidCommand<Data> {

        private final ZonedDateTime reportedTime;
        private final String reportedBySecret;
        private final List<List<String>> ranking;

        public AddGameCommand(ZonedDateTime reportedTime, String reportedBySecret, List<List<String>> ranking) {
            this.reportedTime = reportedTime;
            this.reportedBySecret = reportedBySecret;
            this.ranking = ranking;
        }

        @Override
        public void executeVoid(Data games) {
            games.addGame(reportedTime, reportedBySecret, ranking);
        }
    }

    private static final class GetGamesCommand implements Query<Data, List<Game>> {

        private final Integer count;

        public GetGamesCommand(Integer count) {
            this.count = count;
        }

        @Override
        public List<Game> evaluate(Data games) {
            return games.getGames(count);
        }
    }

    private static final class AddPlayerCommand implements VoidCommand<Data> {

        private final Player player;
        private final String secret;

        AddPlayerCommand(Player player, String secret) {
            this.player = player;
            this.secret = secret;
        }

        @Override
        public void executeVoid(Data data) {
            data.addPlayer(player, secret);
        }
    }

    private static final class GetAllSortedCommand implements Query<Data, List<Player>> {

        @Override
        public List<Player> evaluate(Data data) {
            return data.getAllSorted();
        }
    }

}
