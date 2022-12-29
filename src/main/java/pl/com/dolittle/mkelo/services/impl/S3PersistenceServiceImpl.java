package pl.com.dolittle.mkelo.services.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pl.com.dolittle.mkelo.entity.Game;
import pl.com.dolittle.mkelo.services.PersistenceService;

import java.io.IOException;

@Service
@Slf4j
public class S3PersistenceServiceImpl implements PersistenceService {

    @Autowired
    AmazonS3 amazonS3;
    @Value("${application.bucket.name}")
    private String s3BucketName;

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
    public Game downloadData(String filename) {
        // Create a new class and map it directly
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            S3Object s3Object = amazonS3.getObject(new GetObjectRequest(s3BucketName, filename));
            return objectMapper.readValue(s3Object.getObjectContent(), Game.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void uploadData(String filename, JSONObject jsonObject) {
        try {
            PutObjectResult por = amazonS3.putObject(s3BucketName, filename, jsonObject.toString());
            ObjectMetadata om = por.getMetadata();
            log.info("Uploaded {} ({}) with a size of {} bytes", filename, om.getETag(), om.getContentLength());
        } catch (AmazonServiceException e) {
            throw new AmazonServiceException(e.getErrorMessage());
        }
    }
}
