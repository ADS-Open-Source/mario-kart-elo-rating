package pl.com.dolittle.mkelo.services;

import java.io.InputStream;

public interface PersistenceService {

    byte[] downloadFile(String filename);

    void uploadData(String key, InputStream inputStream);

    InputStream getInsertStatements();

    void uploadInsertsDataToS3();

    void executeInsertStatementsFromS3(String filename);
}
