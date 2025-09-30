package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.service.PersonService;

@ApplicationScoped
@Path("/person/update")
@PermitAll
public class UpdatePerson {
    private static final JsonbConfig JSONB_CONFIG = new JsonbConfig();
    private static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);

    @Inject
    private PersonService personService;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updatePerson(String json) {
        Person person = JSONB.fromJson(json, Person.class);
        this.personService.update(person);
        return Response.ok().build();
    }
}
