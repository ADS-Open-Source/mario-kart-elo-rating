package pl.com.dolittle.mkelo.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.services.PersistenceService;

import javax.sql.DataSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
@Slf4j
public class S3PersistenceServiceImpl implements PersistenceService {

    @Autowired
    AmazonS3 amazonS3;

    @Autowired
    private DataSource dataSource;

    @Value("${application.bucket.name}")
    private String s3BucketName;

    @Value("${bucket-data-filename}")
    private String defaultFilename;

    @Override
    public byte[] downloadFile(String filename) {
        S3Object object = amazonS3.getObject(s3BucketName, filename);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadData(String key, InputStream data) {
        ObjectMetadata metadata = new ObjectMetadata();
        try {
            metadata.setContentLength(data.available());
        } catch (IOException e) {
            log.error(e.getMessage());
            return;
        }

        try {
            PutObjectResult por = amazonS3.putObject(s3BucketName, key, data, metadata);
            ObjectMetadata resultMetadata = por.getMetadata();
            log.info("Uploaded {} ({}) with a size of {} bytes", key, resultMetadata.getETag(), resultMetadata.getContentLength());
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException(e.getErrorMessage());
        }
    }

    @Override
    public void uploadInsertsDataToS3() {
        uploadData(defaultFilename, getInsertStatements());
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
    public void executeInsertStatementsFromS3(String filename) {
        if (filename == null) {
            filename = this.defaultFilename;
        }
        String fileContent = new String(downloadFile(filename), StandardCharsets.UTF_8);
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
