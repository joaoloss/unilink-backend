package com.unilink.api.service;

import java.io.InputStream;
import java.util.Base64;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
public class R2StorageService {

    private final S3Client r2S3Client;
    private final String r2BucketName;
    private final String r2PublicBaseUrl;

    @Autowired
    public R2StorageService(
            S3Client r2S3Client,
            @Qualifier("r2BucketName") String r2BucketName,
            @Qualifier("r2PublicBaseUrl") String r2PublicBaseUrl) {
        this.r2S3Client = r2S3Client;
        this.r2BucketName = r2BucketName;
        this.r2PublicBaseUrl = r2PublicBaseUrl;
    }

    private String upload(InputStream inputStream, long contentLength, String contentType) {
        String key = generateSimpleKey();
        
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(r2BucketName)
                .key(key)
                .contentType(contentType)
                .build();

        r2S3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, contentLength));
        return String.format("%s/%s", r2PublicBaseUrl, key);
    }

    private String uploadBase64(String base64Data, String contentType) {
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        String key = generateSimpleKey();
        
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(r2BucketName)
                .key(key)
                .contentType(contentType)
                .build();

        r2S3Client.putObject(putRequest, RequestBody.fromBytes(imageBytes));
        return String.format("%s/%s", r2PublicBaseUrl, key);
    }

    public String uploadFromUri(String uriWithBase64, String contentType) {
        // Extract base64 from URI (assumed format: data:image/jpeg;base64,/9j/4AAQ...)
        String base64Data = extractBase64FromUri(uriWithBase64);
        return uploadBase64(base64Data, contentType);
    }

    public void delete(String key) {
        DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
                .bucket(r2BucketName)
                .key(key)
                .build();
        r2S3Client.deleteObject(deleteRequest);
    }

    public void deleteByUrl(String imageUrl) {
        if (imageUrl != null && imageUrl.startsWith(r2PublicBaseUrl)) {
            String key = imageUrl.substring(r2PublicBaseUrl.length() + 1); // +1 to remove "/"
            delete(key);
        }
    }

    public String getPublicUrl(String key) {
        return String.format("%s/%s", r2PublicBaseUrl, key);
    }

    private String generateSimpleKey() {
        return UUID.randomUUID().toString();
    }

    private String extractBase64FromUri(String uriWithBase64) {
        if (uriWithBase64 == null || uriWithBase64.isEmpty()) {
            throw new IllegalArgumentException("URI cannot be null or empty");
        }
        
        // Look for "base64," in the URI and extract the content after it
        int base64Index = uriWithBase64.indexOf("base64,");
        if (base64Index == -1) {
            throw new IllegalArgumentException("URI does not contain valid base64 data");
        }
        
        return uriWithBase64.substring(base64Index + 7); // +7 to skip "base64,"
    }
}