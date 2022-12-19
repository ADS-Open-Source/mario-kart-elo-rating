package pl.com.dolittle.mkelo.services;

import org.json.JSONObject;
import pl.com.dolittle.mkelo.entity.Game;

import java.io.IOException;

public interface PersistenceService {

    byte[] downloadFile(String filename);

    void uploadData(String filename, JSONObject jsonObject);

    Game downloadData(String filename) throws IOException;
}
