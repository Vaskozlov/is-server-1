// Updated file: org.vaskozov.is.lab1.service.MinioService.java
package org.vaskozov.is.lab1.service;

import io.minio.*;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.NotFoundException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class MinioService implements MinioServiceInterface {
    private MinioClient minioClient;
    private static final String BUCKET_NAME = "imports";

    public MinioClient getMinioClient() {
        return minioClient;
    }

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

    @Override
    public void uploadFile(String objectName, InputStream inputStream) {
        try {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .stream(inputStream, -1, 10485760)  // 10MB part size
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file to MinIO", e);
        }
    }

    @Override
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

    @Override
    public InputStream getFileStream(String objectName) {
        try {
            return minioClient.getObject(
                    GetObjectArgs.builder()
                            .bucket(BUCKET_NAME)
                            .object(objectName)
                            .build()
            );
        } catch (Exception e) {
            throw new NotFoundException("Failed to get file from MinIO", e);
        }
    }
}