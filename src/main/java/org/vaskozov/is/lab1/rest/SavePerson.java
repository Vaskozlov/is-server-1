package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.service.PersonService;

@ApplicationScoped
@Path("/person/save")
@PermitAll
public class SavePerson {
    @Inject
    private PersonService personService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response savePerson(Person person) {
        try {
            var creationResult = personService.create(person);

            if (creationResult.isError()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(creationResult.getError())
                        .build();
            }

            return Response
                    .status(Response.Status.CREATED)
                    .entity(creationResult.getValue())
                    .build();
        } catch (IllegalStateException e) {
            return savePerson(person);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
