package org.vaskozov.is.lab1.service;

import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class MinioService {
    private MinioClient minioClient;
    private static final String BUCKET_NAME = "imports";

    @PostConstruct
    public void init() {
        minioClient = MinioClient
                .builder()
                .endpoint("http://localhost:9000")
                .credentials("minioadmin", "minioadmin")
                .build();

        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(BUCKET_NAME).build());

            if (!found) {
                minioClient.makeBucket(
                        MakeBucketArgs
                                .builder()
                                .bucket(BUCKET_NAME)
                                .build()
                );
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize MinIO bucket", e);
        }
    }

    public String uploadFile(String objectName, InputStream input, long size, String contentType) {
        try {
            minioClient.putObject(
                    PutObjectArgs
                            .builder()
                            .object(objectName)
                            .bucket(BUCKET_NAME)
                            .stream(input, size, -1)
                            .contentType(contentType)
                            .build()
            );

            return getPresignedUrl(objectName);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            throw new RuntimeException("Failed to upload file to MinIO: " + e.getMessage(), e);
        }
    }

    public String getPresignedUrl(String objectName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.GET)
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .expiry(7, TimeUnit.DAYS)  // Ссылка на 7 дней
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate presigned URL", e);
        }
    }
}
