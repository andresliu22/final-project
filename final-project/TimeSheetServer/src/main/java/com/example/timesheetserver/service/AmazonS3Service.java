package com.example.timesheetserver.service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class AmazonS3Service {

    private Logger logger = LoggerFactory.getLogger(AmazonS3Service.class);

    @Autowired
    private AmazonClientService s3Client;

    @Value("${amazon.s3.bucket-name}")
    private String bucketName;

    @Value("${amazon.s3.endpoint}")
    private String url;

    public String uploadFile(MultipartFile file) {
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            s3Client.getClient().putObject(bucketName, file.getOriginalFilename(), file.getInputStream(), metadata);
            return url + file.getOriginalFilename();
       } catch (Exception ioe) {
            logger.error("IOException: " + ioe.getMessage());
        }
//        } catch (AmazonServiceException serviceException) {
//            logger.info("AmazonServiceException: " + serviceException.getMessage());
//            throw serviceException;
//        } catch (AmazonClientException clientException) {
//            logger.info("AmazonClientException Message: " + clientException.getMessage());
//            throw clientException;
//        }
        return "File not uploaded: " + file.getOriginalFilename();
    }
}