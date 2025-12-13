package org.vaskozov.is.lab1.validation;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.lib.Result;

@ApplicationScoped
public class PersonValidator {
    @Inject
    private LocationValidator locationValidator;

    @Inject
    private CoordinatesValidator coordinatesValidator;

    private static Result<Void, String> constructBadRequest(String message) {
        return Result.error(message);
    }

    public Result<Void, String> validate(Person person) {
        if (person.getName() == null || person.getName().isBlank()) {
            return constructBadRequest("Person's name can not be blank");
        }

        if (person.getCoordinates() == null) {
            return constructBadRequest("Person's coordinates can not be null");
        }

        var coordinatesValidationResult = coordinatesValidator.validate(person.getCoordinates());

        if (coordinatesValidationResult.isError()) {
            return coordinatesValidationResult;
        }

        if (person.getEyeColor() == null) {
            return constructBadRequest("Person's eye color can not be null");
        }

        if (person.getHairColor() == null) {
            return constructBadRequest("Person's hair color can not be null");
        }

        var locationValidationResult = locationValidator.validate(person.getLocation());

        if (locationValidationResult.isError()) {
            return locationValidationResult;
        }

        if (person.getHeight() == null) {
            return constructBadRequest("Person's height can not be null");
        }

        if (person.getHeight() <= 0) {
            return constructBadRequest("Person's height must be positive");
        }

        if (person.getWeight() == null) {
            return constructBadRequest("Person's weight can not be null");
        }

        if (person.getWeight() <= 0) {
            return constructBadRequest("Person's weight must be positive");
        }

        return Result.success(null);
    }
}
