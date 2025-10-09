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

@ApplicationScoped
@Path("/person/delete")
@PermitAll
public class DeletePerson {
    @Inject
    private PersonService personService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deletePerson(Person person) {
        if (person.getId() == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Deleted person ID can not be null").build();
        }

        try {
            personService.delete(person);
            return Response.ok().build();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
