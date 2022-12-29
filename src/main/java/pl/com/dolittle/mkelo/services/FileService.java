package pl.com.dolittle.mkelo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileService {

    private final PersistenceService persistenceService;
    @Value("${bucket-data-filename}")
    private String filename;
    private static final String LOCAL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String PLAYERS_KEY = "players";
    private static final String GAMES_KEY = "games";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public FileService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setDateFormat(new SimpleDateFormat(LOCAL_DATE_PATTERN));
    }

    public List<Player> getPlayersDataFromS3() {

        List<Player> playerList = new LinkedList<>();
        byte[] playersFile = persistenceService.downloadFile(filename);

        JSONObject jsonObject = new JSONObject(new String(playersFile));
        JSONArray jsonArray = jsonObject.getJSONArray(PLAYERS_KEY);

        try {
            playerList = objectMapper.readValue(jsonArray.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return playerList;
    }

    public List<Game> getGamesDataFromS3() {

        List<Game> gameList = new LinkedList<>();
        byte[] playersFile = persistenceService.downloadFile(filename);

        JSONObject jsonObject = new JSONObject(new String(playersFile));
        JSONArray jsonArray = jsonObject.getJSONArray(GAMES_KEY);

        try {
            gameList = objectMapper.readValue(jsonArray.toString(), new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return gameList;
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

