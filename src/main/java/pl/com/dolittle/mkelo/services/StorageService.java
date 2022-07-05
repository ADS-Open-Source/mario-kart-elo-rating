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
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class StorageService{

    private static final Logger LOG = LoggerFactory.getLogger(StorageService.class);

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${application.bucket.name}")
    private String s3BucketName;

    public byte[] downloadFile(String fileName) {
        S3Object object = amazonS3.getObject(s3BucketName,fileName);
        S3ObjectInputStream objectContent = object.getObjectContent();
        try {
            return IOUtils.toByteArray(objectContent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String uploadFile(final MultipartFile multipartFile) {
        try {
            File file = convertMultiPartFileToFile(multipartFile);
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

    private File convertMultiPartFileToFile(final MultipartFile multipartFile) {
        File file = new File(multipartFile.getOriginalFilename());
        try (final FileOutputStream outputStream = new FileOutputStream(file)) {
            outputStream.write(multipartFile.getBytes());
        } catch (IOException e) {
            LOG.error("Error {} occurred while converting the multipart file", e.getLocalizedMessage());
        }
        return file;
    }

}
