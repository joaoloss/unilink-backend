package com.unilink.api.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3Configuration;

@Configuration
public class R2Config {

    @Value("${r2.account-id}")
    private String accountId;

    @Value("${r2.access-key-id}")
    private String accessKeyId;

    @Value("${r2.secret-access-key}")
    private String secretAccessKey;

    @Value("${r2.region:auto}")
    private String region;

    @Value("${r2.bucket}")
    private String bucketName;

    @Value("${r2.public-base-url:}")
    private String publicBaseUrl;

    @Bean
    public S3Client r2S3Client() {
        AwsBasicCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretAccessKey);

        String endpoint = String.format("https://%s.r2.cloudflarestorage.com", accountId);

        return S3Client.builder()
                .credentialsProvider(StaticCredentialsProvider.create(credentials))
                .region(Region.of(region))
                .endpointOverride(URI.create(endpoint))
                .serviceConfiguration(S3Configuration.builder()
                        .pathStyleAccessEnabled(true)
                        .build())
                .build();
    }

    @Bean(name = "r2BucketName")
    public String r2BucketName() {
        return bucketName;
    }

    @Bean(name = "r2PublicBaseUrl")
    public String r2PublicBaseUrl() {
        if (publicBaseUrl != null && !publicBaseUrl.isBlank()) {
            return publicBaseUrl;
        }
        // Default public URL pattern: https://<bucket>.<account-id>.r2.cloudflarestorage.com
        return String.format("https://%s.%s.r2.cloudflarestorage.com", bucketName, accountId);
    }
}