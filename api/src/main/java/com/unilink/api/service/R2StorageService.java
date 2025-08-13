package com.unilink.api.service;

import java.io.InputStream;
import java.net.URLConnection;
import java.time.Instant;
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

    public String upload(InputStream inputStream, long contentLength, String originalFileName, String contentTypeHint) {
        String key = buildObjectKey(originalFileName);
        String contentType = resolveContentType(originalFileName, contentTypeHint);

        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(r2BucketName)
                .key(key)
                .contentType(contentType)
                .build();

        r2S3Client.putObject(putRequest, RequestBody.fromInputStream(inputStream, contentLength));
        return String.format("%s/%s", r2PublicBaseUrl, key);
    }

    public String uploadBase64(String base64Data, String fileName, String contentType) {
        byte[] imageBytes = Base64.getDecoder().decode(base64Data);
        String key = buildObjectKey(fileName);
        
        PutObjectRequest putRequest = PutObjectRequest.builder()
                .bucket(r2BucketName)
                .key(key)
                .contentType(contentType)
                .build();

        r2S3Client.putObject(putRequest, RequestBody.fromBytes(imageBytes));
        return String.format("%s/%s", r2PublicBaseUrl, key);
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
            String key = imageUrl.substring(r2PublicBaseUrl.length() + 1); // +1 para remover a "/"
            delete(key);
        }
    }

    public String getPublicUrl(String key) {
        return String.format("%s/%s", r2PublicBaseUrl, key);
    }

    private String buildObjectKey(String originalFileName) {
        String sanitized = originalFileName == null ? "file" : originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
        String prefix = Instant.now().toEpochMilli() + "-" + UUID.randomUUID();
        return prefix + "/" + sanitized;
    }

    private String resolveContentType(String fileName, String contentTypeHint) {
        if (contentTypeHint != null && !contentTypeHint.isBlank()) {
            return contentTypeHint;
        }
        String guessed = URLConnection.guessContentTypeFromName(fileName);
        return guessed != null ? guessed : "application/octet-stream";
    }
}