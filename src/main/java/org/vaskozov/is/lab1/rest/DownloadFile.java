package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.vaskozov.is.lab1.service.MinioService;

import java.io.InputStream;

@ApplicationScoped
@Path("/download")
@PermitAll
public class DownloadFile {
    @Inject
    private MinioService minioService;

    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@QueryParam("objectName") String objectName) {
        try {
            InputStream fileStream = minioService.getFileStream(objectName);

            StreamingOutput stream = output -> {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = fileStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
                fileStream.close();
            };

            return Response.ok(stream)
                    .header("Content-Disposition", "attachment; filename=\"" + objectName + "\"")
                    .build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Failed to download file").build();
        }
    }
}