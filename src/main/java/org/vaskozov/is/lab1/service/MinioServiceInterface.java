package org.vaskozov.is.lab1.service;

import jakarta.ws.rs.NotFoundException;

import java.io.InputStream;

public interface MinioServiceInterface {
    void uploadFile(String objectName, InputStream inputStream);

    String getPresignedUrl(String objectName);

    InputStream getFileStream(String objectName) throws NotFoundException;
}
