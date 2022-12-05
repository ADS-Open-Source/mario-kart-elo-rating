package pl.com.dolittle.mkelo.services;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class StorageService {

    private static final Logger LOG = LoggerFactory.getLogger(StorageService.class);
    @Autowired
    private AmazonS3 amazonS3;
    @Value("${application.bucket.name}")
    private String s3BucketName;

    public byte[] downloadFile(String fileName) {
        S3Object object = amazonS3.getObject(s3BucketName, fileName);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String uploadFile(final File file) {
        try {
            String fileName = file.getName();
            LOG.info("Uploading file with name {}", fileName);

            PutObjectRequest putObjectRequest = new PutObjectRequest(s3BucketName, fileName, file);
            amazonS3.putObject(putObjectRequest);
            file.delete();
            return "File uploaded: " + fileName;
        } catch (AmazonServiceException e) {
            throw new RuntimeException(e);
        }
    }

}
