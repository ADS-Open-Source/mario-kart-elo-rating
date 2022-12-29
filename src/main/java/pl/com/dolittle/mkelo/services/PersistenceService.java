package pl.com.dolittle.mkelo.services;

import org.json.JSONObject;
import pl.com.dolittle.mkelo.entity.MKEloData;

import java.io.IOException;

public interface PersistenceService {

    byte[] downloadFile(String filename);

    void uploadData(String filename, JSONObject jsonObject);

    MKEloData downloadData(String filename) throws IOException;
}
