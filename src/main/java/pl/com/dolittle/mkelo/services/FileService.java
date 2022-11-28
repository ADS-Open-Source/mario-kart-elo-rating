package pl.com.dolittle.mkelo.services;

import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.entity.Player;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class FileService {

    @Autowired
    private StorageService storageService;
    private static final String FILENAME = "mkeloData.json";
    private static final String FILEPATH = "mario-kart-elo-rating\\mkeloData.json";
    private static final String LOCAL_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String PLAYERS_KEY = "players";
    private static final String GAMES_KEY = "games";

    public Map<String, Player> getPlayersDataFromS3() {

        Gson gson = new Gson();
        Map<String, Player> map = new HashMap<>();

        if (storageService != null) {
            byte[] playersFile = storageService.downloadFile(FILENAME);

            JSONObject jsonObject = new JSONObject(new String(playersFile));
            JSONArray jsonArray = jsonObject.getJSONArray(PLAYERS_KEY);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                map.put(obj.getString("key"), gson.fromJson(String.valueOf(obj.getJSONObject("value")), Player.class));
            }
            return map;
        }
        return map;
    }

    public List<Game> getGamesDataFromS3() {

        LinkedList<Game> gameList = new LinkedList<>();

        if (storageService != null) {
            byte[] playersFile = storageService.downloadFile(FILENAME);
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

    public void putPlayersDataToS3(Map<String, Player> playersToS3) {

        List<Game> gamesToS3 = getGamesDataFromS3();
        putDataToS3(playersToS3, gamesToS3);
    }

    public void putGamesDataToS3(List<Game> gamesToS3) {

        Map<String, Player> playersToS3 = getPlayersDataFromS3();
        putDataToS3(playersToS3, gamesToS3);
    }

    private void putDataToS3(Map<String, Player> playersToS3, List<Game> gamesToS3) {
        Set<String> playerKeys = playersToS3.keySet();
        Collection<Player> playerValues = playersToS3.values();
        JSONArray playersJsonArray = new JSONArray();
        JSONObject jsonData = new JSONObject();
        Gson gson = new Gson();

        for (int i = 0; i < playersToS3.size(); i++) {
            JSONObject playerDataObject = new JSONObject();
            playerDataObject.put("key", playerKeys.toArray()[i]);
            String playerValueJson = gson.toJson(playerValues.toArray()[i]);
            JSONObject playerValueObject = new JSONObject(playerValueJson);
            playerDataObject.put("value", playerValueObject);
            playersJsonArray.put(playerDataObject);
        }
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.setDateFormat(new SimpleDateFormat(LOCAL_DATE_PATTERN));
        try {
            String gamesString = mapper.writeValueAsString(gamesToS3);
            JSONArray gamesJsonArray = new JSONArray(gamesString);
            jsonData.put(PLAYERS_KEY, playersJsonArray);
            jsonData.put(GAMES_KEY, gamesJsonArray);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        writeJsonToFile(jsonData);
        uploadJsonToS3();
    }

    private void writeJsonToFile(JSONObject jsonData) {
        try (FileWriter fw = new FileWriter(FILEPATH)) {
            fw.write(jsonData.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadJsonToS3() {
        File file = new File(FILEPATH);
        try {
            FileItem fileItem = new DiskFileItem("mainFile", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
            IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
            MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
            storageService.uploadFile(multipartFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

