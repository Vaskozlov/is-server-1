package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.bean.User;
import org.vaskozov.is.lab1.repository.OperationRepository;
import org.vaskozov.is.lab1.repository.UserRepository;

import java.util.Optional;

@ApplicationScoped
@Path("/operations")
@PermitAll
public class GetOperations {
    private static final JsonbConfig JSONB_CONFIG = new JsonbConfig();
    private static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);

    @Inject
    private OperationRepository operationRepository;

    @Inject
    private UserRepository userRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getOperations(@QueryParam("username") String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isEmpty()) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .build();
        }

        return Response
                .ok(JSONB.toJson(operationRepository.findByUser(user.get())))
                .build();
    }
}
