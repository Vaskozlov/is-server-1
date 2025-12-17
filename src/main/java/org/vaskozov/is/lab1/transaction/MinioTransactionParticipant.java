package org.vaskozov.is.lab1.transaction;

import io.minio.*;
import org.vaskozov.is.lab1.service.MinioService;

import java.io.InputStream;

public class MinioTransactionParticipant implements TransactionParticipant {
    private final MinioService minioService;
    private final String bucketName;
    private final String objectName;
    private final InputStream inputStream;
    private final long contentLength;
    private final String contentType;

    private final String pendingObjectName;

    public MinioTransactionParticipant(
            MinioService minioService,
            String bucketName,
            String objectName,
            InputStream inputStream,
            long contentLength,
            String contentType
    ) {
        this.minioService = minioService;
        this.bucketName = bucketName;
        this.objectName = objectName;
        this.inputStream = inputStream;
        this.contentLength = contentLength;
        this.contentType = contentType;
        this.pendingObjectName = "pending/" + objectName;
    }

    @Override
    public void prepare() throws Exception {
        minioService.getMinioClient().putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(pendingObjectName)
                        .stream(inputStream, contentLength, -1)
                        .contentType(contentType)
                        .build()
        );
    }

    @Override
    public void commit() throws Exception {
        minioService.getMinioClient().copyObject(
                CopyObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .source(CopySource.builder().bucket(bucketName).object(pendingObjectName).build())
                        .build()
        );
        minioService.getMinioClient().removeObject(
                RemoveObjectArgs.builder().bucket(bucketName).object(pendingObjectName).build()
        );
    }

    @Override
    public void rollback() {
        try {
            minioService.getMinioClient().statObject(StatObjectArgs.builder().bucket(bucketName).object(pendingObjectName).build());
            minioService.getMinioClient().removeObject(RemoveObjectArgs.builder().bucket(bucketName).object(pendingObjectName).build());
        } catch (Exception e) {
        }
    }
}