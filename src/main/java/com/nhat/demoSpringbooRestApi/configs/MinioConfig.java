package com.nhat.demoSpringbooRestApi.configs;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MinioConfig {
    @Value("${minio.access.name}")
    String accessKey;
    @Value("${minio.access.secret}")
    String accessSecret;
    @Value("${minio.url}")
    String minioUrl;

    @Bean
    public MinioClient generateMinioClient() {
        try {
//            MinioClient client = new MinioClient(minioUrl, accessKey, accessSecret);
            return MinioClient.builder()
                    .credentials(accessKey, accessSecret)
                    .endpoint(minioUrl)
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }


}