package pl.com.dolittle.mkelo.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class FileService {

    @Autowired
    private PersistenceService persistenceService;
    @Value("${bucket-data-filename}")
    private String FILENAME;
    private static final String LOCAL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String PLAYERS_KEY = "players";
    private static final String GAMES_KEY = "games";

    public List<Player> getPlayersDataFromS3() {

        List<Player> players = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        if (persistenceService != null) {
            byte[] playersFile = persistenceService.downloadFile(FILENAME);

            JSONObject jsonObject = new JSONObject(new String(playersFile));
            JSONArray jsonArray = jsonObject.getJSONArray(PLAYERS_KEY);

            for (int i = 0; i < jsonArray.length(); i++) {
                String obj = jsonArray.getJSONObject(i).toString();
                try {
                    players.add(objectMapper.readValue(obj, Player.class));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return players;
        }
        return players;
    }

    public List<Game> getGamesDataFromS3() {

        LinkedList<Game> gameList = new LinkedList<>();

        if (persistenceService != null) {
            byte[] playersFile = persistenceService.downloadFile(FILENAME);

            JSONObject jsonObject = new JSONObject(new String(playersFile));
            JSONArray jsonArray = jsonObject.getJSONArray(GAMES_KEY);

            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.setDateFormat(new SimpleDateFormat(LOCAL_DATE_PATTERN));
            try {
                gameList = objectMapper.readValue(jsonArray.toString(), new TypeReference<>() {
                });
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            return gameList;
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
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat(LOCAL_DATE_PATTERN));
        try {
            String playersString = mapper.writeValueAsString(playersToS3);
            JSONArray playersJsonArray = new JSONArray(playersString);
            String gamesString = mapper.writeValueAsString(gamesToS3);
            JSONArray gamesJsonArray = new JSONArray(gamesString);
            jsonData.put(PLAYERS_KEY, playersJsonArray);
            jsonData.put(GAMES_KEY, gamesJsonArray);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        persistenceService.uploadData(FILENAME, jsonData);
    }
}

