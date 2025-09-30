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
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.service.PersonService;

@ApplicationScoped
@Path("/person/get_persons")
@PermitAll
public class GetPersons {
    private static final JsonbConfig JSONB_CONFIG = new JsonbConfig();
    private static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);

    @Inject
    private PersonService personService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersons() {
        return Response.ok(JSONB.toJson(personService.getPersons())).build();
    }
}
