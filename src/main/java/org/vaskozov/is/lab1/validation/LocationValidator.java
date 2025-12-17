package org.vaskozov.is.lab1.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.vaskozov.is.lab1.bean.Location;
import org.vaskozov.is.lab1.util.Result;
import org.vaskozov.is.lab1.repository.LocationRepository;

@ApplicationScoped
public class LocationValidator {
    @Inject
    private LocationRepository locationRepository;

    public Result<Void, String> validate(Location location) {
        if (location.getName().length() > 409) {
            return Result.error("Location.name must be less than 409");
        }

        if (location.getId() != null && locationRepository.findById(location.getId()).isEmpty()) {
            return Result.error("Location with id " + location.getId() + " does not exist");
        }

        if (location.getX() < 0 || location.getX() >= 360.0) {
            return Result.error("Location.x must be between 0 and 360");
        }

        if (location.getY() < 0 || location.getY() >= 180.0) {
            return Result.error("Location.y must be between 0 and 180");
        }

        return Result.success(null);
    }
}
