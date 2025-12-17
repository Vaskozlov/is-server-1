package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.service.PersonService;
import org.vaskozov.is.lab1.util.JsonbUtil;

@ApplicationScoped
@Path("/person/update")
@PermitAll
public class UpdatePerson {
    @Inject
    private PersonService personService;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePerson(String json) {
        Person person;

        try {
            person = JsonbUtil.fromJson(json, Person.class);
        } catch (JsonbException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to parse JSON").build();
        }

        try {
            var updateResult = this.personService.update(person);

            if (updateResult.isError()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(updateResult.getError())
                        .build();
            }

            return Response.ok().build();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
