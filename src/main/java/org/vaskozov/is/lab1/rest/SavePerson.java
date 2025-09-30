package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.service.PersonService;

import java.time.LocalDateTime;

@ApplicationScoped
@Path("/person/save")
@PermitAll
public class SavePerson {
    @Inject
    private PersonService personService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePerson(Person person) {
        person.setCreationTime(LocalDateTime.now());
        personService.create(person);
        return Response.ok().build();
    }
}
