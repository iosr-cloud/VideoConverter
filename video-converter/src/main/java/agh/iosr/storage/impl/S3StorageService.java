package agh.iosr.storage.impl;

import agh.iosr.storage.api.StorageService;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;

@Service
public class S3StorageService implements StorageService {

    private Logger logger = LoggerFactory.getLogger(S3StorageService.class);

    @Autowired
    private AmazonS3 s3client;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Override
    public URL uploadFile(String filename, File uploadFile) {
        URL fileURL = null;
        try {
            s3client.putObject(bucketName, filename, uploadFile);
            fileURL = s3client.getUrl(bucketName, filename);
            logger.info("Uploaded File: " + filename);
        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }
        return fileURL;
    }
}
