package org.vaskozov.is.lab1.rest;

import jakarta.annotation.security.PermitAll;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.JsonbException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.service.PersonService;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@ApplicationScoped
@Path("/person/save_all")
@PermitAll
public class SavePersons {
    private static final JsonbConfig JSONB_CONFIG = new JsonbConfig();
    private static final Jsonb JSONB = JsonbBuilder.create(JSONB_CONFIG);


    private static final Type listType = new ParameterizedType() {
        public Type[] getActualTypeArguments() {
            return new Type[]{Person.class};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }
    };

    @Inject
    private PersonService personService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response savePersons(String json) {
        List<Person> persons;

        try {
            persons = JSONB.fromJson(json, listType);
        } catch (JsonbException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("Failed to parse JSON")
                    .build();
        }

        try {
            var saveResult = personService.savePersons(persons);

            if (saveResult.isError()) {
                return Response
                        .status(Response.Status.BAD_REQUEST)
                        .entity(saveResult.getError())
                        .build();
            }

            return Response
                    .ok()
                    .entity(JSONB.toJson(saveResult.getValue()))
                    .build();
        } catch (IllegalArgumentException ex) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity(ex.getMessage())
                    .build();
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace();

            return Response
                    .status(Response.Status.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}