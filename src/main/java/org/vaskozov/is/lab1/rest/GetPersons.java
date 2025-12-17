package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.service.PersonService;
import org.vaskozov.is.lab1.util.JsonbUtil;

@ApplicationScoped
@Path("/person/get_persons")
@PermitAll
public class GetPersons {
    @Inject
    private PersonService personService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPersons() {
        return Response.ok(JsonbUtil.toJson(personService.getPersons())).build();
    }
}
