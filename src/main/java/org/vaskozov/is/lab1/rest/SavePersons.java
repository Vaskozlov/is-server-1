package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.transaction.UserTransaction;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.bean.*;
import org.vaskozov.is.lab1.parser.CsvPersonParser;
import org.vaskozov.is.lab1.repository.OperationRepository;
import org.vaskozov.is.lab1.repository.UserRepository;
import org.vaskozov.is.lab1.service.MinioService;
import org.vaskozov.is.lab1.service.PersonService;
import org.vaskozov.is.lab1.transaction.DbTransactionParticipant;
import org.vaskozov.is.lab1.transaction.DistributedTransactionCoordinator;
import org.vaskozov.is.lab1.transaction.MinioTransactionParticipant;
import org.vaskozov.is.lab1.util.JsonbUtil;
import org.vaskozov.is.lab1.util.Result;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
@Path("/person/save_all")
@PermitAll
public class SavePersons {
    @Inject
    private UserRepository userRepository;

    @Inject
    private OperationRepository operationRepository;

    @Inject
    private PersonService personService;

    @Inject
    private CsvPersonParser csvPersonParser;

    @Inject
    private MinioService minioService;

    @Inject
    private UserTransaction userTransaction;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePersons(String jsonString) {
        try (JsonReader jsonReader = Json.createReader(new StringReader(jsonString))) {
            JsonObject jsonObject = jsonReader.readObject();
            String username = jsonObject.getString("username");
            String content = jsonObject.getString("content");

            Optional<User> userOptional = userRepository.findByUsername(username);

            if (userOptional.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }

            User user = userOptional.get();
            String objectName = UUID.randomUUID() + ".csv";
            String bucketName = "imports";
            String contentType = "text/csv";

            long contentLength = content.getBytes(StandardCharsets.UTF_8).length;
            InputStream contentStream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));

            DistributedTransactionCoordinator coordinator = new DistributedTransactionCoordinator();

            MinioTransactionParticipant minioParticipant = new MinioTransactionParticipant(
                    minioService, bucketName, objectName, contentStream, contentLength, contentType
            );

            coordinator.addParticipant(minioParticipant);

            DbTransactionParticipant<Operation> dbParticipant = new DbTransactionParticipant<>(userTransaction, () -> {
                try (InputStream pendingStream = minioService.getFileStream("pending/" + objectName)) {
                    Result<List<Person>, String> parseResult = csvPersonParser.parseCsv(content);

                    if (parseResult.isError()) {
                        throw new RuntimeException("Parse error: " + parseResult.getError());
                    }

                    List<Person> persons = parseResult.getValue();

                    Operation operation = Operation.builder()
                            .type(OperationType.FILE_UPLOAD)
                            .status(OperationStatus.SUCCESS)
                            .user(user)
                            .objectName(objectName)
                            .changes((long) persons.size())
                            .build();

                    operationRepository.save(operation);

                    Result<List<Person>, String> saveResult = personService.create(persons);

                    if (saveResult.isError()) {
                        throw new RuntimeException("Save error: " + saveResult.getError());
                    }

                    personService.broadcastChangedPersons(persons);

                    return operation;
                } catch (Exception e) {
                    throw new RuntimeException("DB operation failed", e);
                }
            });

            coordinator.addParticipant(dbParticipant);

            Operation resultOperation = coordinator.coordinate(dbParticipant::getResult);
            return Response.ok(JsonbUtil.toJson(resultOperation)).build();
        } catch (Exception e) {
            System.err.println("Transaction failed: " + e.getMessage());
            e.printStackTrace(System.err);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Transaction failed").build();
        }
    }
}