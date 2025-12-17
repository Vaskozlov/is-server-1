package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.bean.Operation;
import org.vaskozov.is.lab1.bean.OperationStatus;
import org.vaskozov.is.lab1.bean.OperationType;
import org.vaskozov.is.lab1.bean.User;
import org.vaskozov.is.lab1.parser.CsvPersonParser;
import org.vaskozov.is.lab1.repository.OperationRepository;
import org.vaskozov.is.lab1.repository.UserRepository;
import org.vaskozov.is.lab1.service.MinioService;
import org.vaskozov.is.lab1.service.PersonService;
import org.vaskozov.is.lab1.util.JsonbUtil;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Path("/person/save_all")
@PermitAll
public class SavePersons {
    @Inject
    private PersonService personService;

    @Inject
    private UserRepository userRepository;

    @Inject
    private CsvPersonParser csvPersonParser;

    @Inject
    private MinioService minioService;
    @Inject
    private OperationRepository operationRepository;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePersons(String jsonBody) {
        try (JsonReader jsonReader = Json.createReader(new ByteArrayInputStream(jsonBody.getBytes(StandardCharsets.UTF_8)))) {
            JsonObject jsonObject = jsonReader.readObject();
            String username = jsonObject.getString("username", null).trim();
            String csvContent = jsonObject.getString("content", null);

            if (username.isBlank()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Username is required")
                        .build();
            }

            Optional<User> userOpt = userRepository.findByUsername(username);

            if (userOpt.isEmpty()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("Username not found")
                        .build();
            }

            User user = userOpt.get();

            if (csvContent == null) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("csv content")
                        .build();
            }

            var parseResult = csvPersonParser.parsePersonCsv(csvContent);

            if (parseResult.isError()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(parseResult.getError())
                        .build();
            }

            var persons = parseResult.getValue();

            if (persons.isEmpty()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity("No valid persons found in CSV")
                        .build();
            }

            var saveResult = personService.create(persons);

            String objectName = UUID.randomUUID() + ".csv";
            byte[] fileBytes = csvContent.getBytes(StandardCharsets.UTF_8);

            InputStream uploadStream = new ByteArrayInputStream(fileBytes);
            minioService.uploadFile(objectName, uploadStream);

            Operation operation = Operation
                    .builder()
                    .type(OperationType.FILE_UPLOAD)
                    .status(OperationStatus.SUCCESS)
                    .user(user)
                    .objectName(objectName)
                    .changes((long) persons.size())
                    .build();

            operationRepository.save(operation);

            if (saveResult.isError()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(saveResult.getError())
                        .build();
            }

            return Response
                    .ok()
                    .entity(JsonbUtil.JSONB.toJson(operation))
                    .build();
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}