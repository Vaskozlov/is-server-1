package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.service.PersonServiceInterface;

@ApplicationScoped
@Path("/person/delete")
@PermitAll
public class DeletePerson {
    @Inject
    private PersonServiceInterface personService;

    @DELETE
    public Response deletePerson(@QueryParam("id") Long id) {
        if (id == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Deleted person ID can not be null").build();
        }

        try {
            personService.delete(id);
            return Response.ok().build();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
