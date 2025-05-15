package com.tsix_hack.spam_ai_detection.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;

@Service
public class BucketService {
    private final AmazonS3 s3Client;
    @Value("${aws.s3.bucket}")
    private String bucketName;

    public BucketService(AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    public String upload( MultipartFile file ) throws IOException {
        var key = file.getOriginalFilename();
        byte[] content = file.getBytes();
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(content.length);
        metadata.setContentType(file.getContentType());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(content);
        s3Client.putObject(bucketName, key, byteArrayInputStream, metadata);
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        GeneratePresignedUrlRequest generatePresignedUrlRequest =
                new GeneratePresignedUrlRequest(bucketName, key)
                        .withMethod(HttpMethod.GET)
                        .withExpiration(expiration);

        URL presignedUrl = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return presignedUrl.toString();
    }

    public URL getPresignedUrl(String bucketName, String key) {
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key)
                .withMethod(HttpMethod.GET)
                .withExpiration(expiration) ;
        return s3Client.generatePresignedUrl(generatePresignedUrlRequest);
    }
}
