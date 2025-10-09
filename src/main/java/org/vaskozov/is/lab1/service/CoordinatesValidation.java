package org.vaskozov.is.lab1.service;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.vaskozov.is.lab1.bean.Coordinates;
import org.vaskozov.is.lab1.lib.Result;

@ApplicationScoped
public class CoordinatesValidation {
    @EJB(name = "java:global/is_lab_1/PersonsDatabase")
    private PersonsDatabaseInterface database;

    public Result<Void, Response> validate(Coordinates coordinates) {
        if (coordinates.getX() != null && coordinates.getX() < -367) {
            return Result.error(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Coordinate.x can not be below -367")
                            .build()
            );
        }

        if (coordinates.getY() != null && coordinates.getY() > 944) {
            return Result.error(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Coordinate.y can not be above 944")
                            .build()
            );
        }

        if (coordinates.getId() != null && !database.hasCoordinate(coordinates.getId())) {
            return Result.error(
                    Response.status(Response.Status.BAD_REQUEST)
                            .entity("Coordinate with id " + coordinates.getId() + " doesn't exist")
                            .build()
            );
        }

        return Result.success(null);
    }
}
