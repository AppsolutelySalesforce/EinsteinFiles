package com.einstein.files.EiFiles;


import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

@Service
public class S3ServicesImpl implements S3Services {

    private final AmazonS3 s3client;
    private Logger logger = LoggerFactory.getLogger(S3ServicesImpl.class);
    @Value("${amazon.s3.bucket}")
    private String bucketName;

    @Autowired
    public S3ServicesImpl(AmazonS3 s3client) {
        this.s3client = s3client;
    }

    @Override
    public S3Object downloadFile(String keyName) {

        try {

            System.out.println("Downloading an object");
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
            System.out.println("Content-Type: " + s3object.getObjectMetadata().getContentType());
//            Utility.displayText(s3object.getObjectContent());
            logger.info("===================== Import File - Done! =====================");
            return s3object;

        } catch (AmazonServiceException ase) {
            logger.info("Caught an AmazonServiceException from GET requests, rejected reasons:");
            logger.info("Error Message:    " + ase.getMessage());
            logger.info("HTTP Status Code: " + ase.getStatusCode());
            logger.info("AWS Error Code:   " + ase.getErrorCode());
            logger.info("Error Type:       " + ase.getErrorType());
            logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
//        } catch (IOException ioe) {
//            logger.info("IOE Error Message: " + ioe.getMessage());
        }
        return null;
    }

    @Override
    public void uploadFileBase64(String fileName, String uploadFileBase64) {

        try {
            byte[] imageBytes = Base64.decodeBase64(uploadFileBase64);
            File file = new File(fileName);
            file.createNewFile();
            new FileOutputStream(file, false).write(imageBytes);

            s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
            logger.info("===================== Upload File - Done! =====================");

        } catch (AmazonServiceException ase) {
            logAmazonError(ase);
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void uploadFile(String fileName, byte[] bites) {

        try {

            byte[] imageBytes = bites;
            File file = new File(fileName);
            file.createNewFile();
            new FileOutputStream(file, false).write(imageBytes);

            s3client.putObject(new PutObjectRequest(bucketName, fileName, file));
            logger.info("===================== Upload File - Done! =====================");

        } catch (AmazonServiceException ase) {
            logAmazonError(ase);
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getURL(String keyName) {

        // Generate the presigned URL.
        System.out.println("Generating pre-signed URL.");
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, keyName)
                        .withMethod(HttpMethod.GET);
        URL url = s3client.generatePresignedUrl(generatePresignedUrlRequest);
        System.out.println(">>> URL > " + url);
        return url.toString();
    }

    private void logAmazonError(AmazonServiceException ase) {
        logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
        logger.info("Error Message:    " + ase.getMessage());
        logger.info("HTTP Status Code: " + ase.getStatusCode());
        logger.info("AWS Error Code:   " + ase.getErrorCode());
        logger.info("Error Type:       " + ase.getErrorType());
        logger.info("Request ID:       " + ase.getRequestId());
    }

}