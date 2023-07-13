package pl.com.dolittle.mkelo.services.impl;

import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.config.AWSConfig;
import pl.com.dolittle.mkelo.services.PersistenceService;

import javax.sql.DataSource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Service
@ConditionalOnMissingBean(AWSConfig.class)
public class LocalPersistenceServiceImpl implements PersistenceService, InitializingBean {

    @Autowired
    private DataSource dataSource;

    @Value("${bucket-data-filename}")
    private String defaultFilename;


    @Override
    public void afterPropertiesSet() {
        log.warn("AWS not configured. Proceeding with local storage at {}", Paths.get(System.getProperty("user.dir"), defaultFilename));
    }

    @Override
    public byte[] downloadFile(String filename) {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), defaultFilename);
            if (!Files.exists(filePath)) {
                log.warn("{} does not exist. Proceeding to create an empty file", filePath);
                Files.createFile(filePath);
            }
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error(e.getMessage());
            return new byte[0];
        }
    }

    @Override
    public void uploadData(String key, InputStream inputStream) {
        try {
            Path filePath = Paths.get(System.getProperty("user.dir"), key);
            File file = filePath.toFile();

            java.nio.file.Files.copy(
                    inputStream,
                    file.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
            );

            IOUtils.closeQuietly(inputStream, null);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public InputStream getInsertStatements() {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement preparedStatement = conn.prepareStatement("SCRIPT SIMPLE NOSETTINGS");
             ByteArrayOutputStream output = new ByteArrayOutputStream()) {

            preparedStatement.executeQuery();
            ResultSet resultSet = preparedStatement.getResultSet();

            while (resultSet.next()) {
                String line = resultSet.getString(1);
                if (line.startsWith("INSERT")) {
                    output.write(line.getBytes());
                    output.write('\n');
                }
            }

            log.info("inserts dumped to input stream");
            return new ByteArrayInputStream(output.toByteArray());

        } catch (SQLException | IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

    @Override
    public void uploadInsertsData() {
        uploadData(defaultFilename, getInsertStatements());
    }

    @Override
    public void executeInsertStatements(String filename) {
        if (filename == null) {
            filename = this.defaultFilename;
        }
        String fileContent = new String(downloadFile(filename), StandardCharsets.UTF_8);
        if (fileContent.isBlank()) {
            log.warn("no insert statements found. Proceeding with empty database");
            return;
        }
        String[] lines = fileContent.split("\n");
        for (String line : lines) {
            if (line.startsWith("INSERT")) {
                try (Connection conn = dataSource.getConnection()) {
                    PreparedStatement statement = conn.prepareStatement(line);
                    statement.execute();
                    log.info("executed: {}", line);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        try (Connection conn = dataSource.getConnection()) {
            //  primary key of games on load starts with 1 which obviously already exists when loading existing games so we need to update it accordingly
            PreparedStatement statement = conn.prepareStatement("ALTER TABLE GAMES ALTER COLUMN ID RESTART WITH (SELECT MAX(ID) + 1 FROM GAMES)");
            statement.execute();
            log.info("updated the games identity");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
