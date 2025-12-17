package org.vaskozov.is.lab1.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.vaskozov.is.lab1.bean.Coordinates;
import org.vaskozov.is.lab1.util.Result;
import org.vaskozov.is.lab1.repository.CoordinatesRepository;

@ApplicationScoped
public class CoordinatesValidator {
    @Inject
    private CoordinatesRepository coordinatesRepository;

    public Result<Void, String> validate(Coordinates coordinates) {
        if (coordinates.getX() != null && coordinates.getX() < -367) {
            return Result.error("Coordinate.x can not be below -367");
        }

        if (coordinates.getY() != null && coordinates.getY() > 944) {
            return Result.error("Coordinate.y can not be above 944");
        }

        if (coordinates.getId() != null && coordinatesRepository.findById(coordinates.getId()).isEmpty()) {
            return Result.error("Coordinate with id " + coordinates.getId() + " doesn't exist");
        }

        return Result.success(null);
    }
}
