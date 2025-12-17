package org.vaskozov.is.lab1.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.vaskozov.is.lab1.bean.Coordinates;
import org.vaskozov.is.lab1.bean.Location;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.repository.CoordinatesRepository;
import org.vaskozov.is.lab1.repository.LocationRepository;

@ApplicationScoped
public class PersonsEntitiesUpdater {
    @Inject
    private CoordinatesRepository coordinatesRepository;

    @Inject
    private LocationRepository locationRepository;

    public void updateCoordinates(Person person, Coordinates coordinates) {
        if (!coordinates.getId().equals(person.getCoordinates().getId())) {
            var existingCoordinate = coordinatesRepository.findById(coordinates.getId()).orElseThrow();
            person.setCoordinates(existingCoordinate);
            return;
        }
        if (coordinates.getX() != null) {
            person.getCoordinates().setX(coordinates.getX());
        }

        if (coordinates.getY() != null) {
            person.getCoordinates().setY(coordinates.getY());
        }
    }

    public void updateLocation(Person person, Location location) {
        if (!location.getId().equals(person.getLocation().getId())) {
            var existingLocation = locationRepository.findById(location.getId()).orElseThrow();
            person.setLocation(existingLocation);
            return;
        }

        if (location.getX() != null) {
            person.getLocation().setX(location.getX());
        }

        if (location.getY() != null) {
            person.getLocation().setY(location.getY());
        }
    }
}
