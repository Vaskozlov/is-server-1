package org.vaskozov.is.lab1.service;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.bean.Location;
import org.vaskozov.is.lab1.lib.Result;

@ApplicationScoped
public class LocationValidation {
    @EJB(name = "java:global/is_lab_1/PersonsDatabase")
    private PersonsDatabaseInterface database;

    public Result<Void, Response> validate(Location location) {
        if (location.getName().length() > 409) {
            return Result.error(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Location.name must be less than 409")
                            .build()
            );
        }

        if (location.getId() != null && !database.hasLocation(location.getId())) {
            return Result.error(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Location with id " + location.getId() + " does not exist")
                            .build()
            );
        }

        return Result.success(null);
    }
}
