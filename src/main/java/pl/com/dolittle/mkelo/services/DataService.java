package pl.com.dolittle.mkelo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.MKEloData;
import pl.com.dolittle.mkelo.entity.Player;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class DataService {

    @Value("${bucket-data-filename}")
    private String filename;
    private final PersistenceService persistenceService;

    private final MKEloData eloData = new MKEloData();

    public static final String LOCAL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String PLAYERS_KEY = "players";
    private static final String GAMES_KEY = "games";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public DataService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
        this.eloData.setFilename(filename);
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.setDateFormat(new SimpleDateFormat(LOCAL_DATE_PATTERN));
    }

    public List<Player> getPlayersDataFromS3() {

        try {
            MKEloData s3EloData = persistenceService.downloadData(filename);
            this.eloData.setPlayers(s3EloData.getPlayers());
            return s3EloData.getPlayers();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Game> getGamesDataFromS3() {

        try {
            MKEloData s3EloData = persistenceService.downloadData(filename);
            this.eloData.setGames(s3EloData.getGames());
            return s3EloData.getGames();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void putPlayersDataToS3(List<Player> playersToS3) {

        List<Game> gamesToS3 = getGamesDataFromS3();
        putDataToS3(playersToS3, gamesToS3);
    }

    public void putGamesDataToS3(List<Game> gamesToS3) {

        List<Player> playersToS3 = getPlayersDataFromS3();
        putDataToS3(playersToS3, gamesToS3);
    }

    private void putDataToS3(List<Player> playersToS3, List<Game> gamesToS3) {

        JSONObject jsonData = new JSONObject();
        try {
            String playersString = objectMapper.writeValueAsString(playersToS3);
            JSONArray playersJsonArray = new JSONArray(playersString);

            String gamesString = objectMapper.writeValueAsString(gamesToS3);
            JSONArray gamesJsonArray = new JSONArray(gamesString);

            jsonData.put(PLAYERS_KEY, playersJsonArray);
            jsonData.put(GAMES_KEY, gamesJsonArray);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        persistenceService.uploadData(filename, jsonData);
    }
}

