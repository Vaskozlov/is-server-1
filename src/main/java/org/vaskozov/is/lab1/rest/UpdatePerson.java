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
import org.vaskozov.is.lab1.service.CoordinatesValidation;
import org.vaskozov.is.lab1.service.LocationValidation;
import org.vaskozov.is.lab1.service.PersonService;

@ApplicationScoped
@Path("/person/update")
@PermitAll
public class UpdatePerson {
    private static final JsonbConfig JSONB_CONFIG = new JsonbConfig();
    private static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);

    @Inject
    private PersonService personService;

    @Inject
    private CoordinatesValidation coordinatesValidation;

    @Inject
    private LocationValidation locationValidation;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    public Response updatePerson(String json) {
        Person person;

        try {
            person = JSONB.fromJson(json, Person.class);
        } catch (JsonbException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failed to parse JSON").build();
        }

        if (person.getName() != null && person.getName().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Name is required").build();
        }

        if (person.getCoordinates() != null) {
            var coordinatesValidationResult = coordinatesValidation.validate(person.getCoordinates());

            if (coordinatesValidationResult.isError()) {
                return coordinatesValidationResult.getError();
            }
        }

        if (person.getLocation() != null) {
            var locationValidationResult = locationValidation.validate(person.getLocation());

            if (locationValidationResult.isError()) {
                return locationValidationResult.getError();
            }
        }

        if (person.getHeight() != null && person.getHeight() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Height must be positive").build();
        }

        if (person.getWeight() != null && person.getWeight() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Weight must be positive").build();
        }

        try {
            this.personService.update(person);
            return Response.ok().build();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
    }
}
