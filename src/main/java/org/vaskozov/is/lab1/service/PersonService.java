package org.vaskozov.is.lab1.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.vaskozov.is.lab1.bean.Coordinates;
import org.vaskozov.is.lab1.bean.Location;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.lib.Result;
import org.vaskozov.is.lab1.repository.*;
import org.vaskozov.is.lab1.validation.CoordinatesValidator;
import org.vaskozov.is.lab1.validation.LocationValidator;
import org.vaskozov.is.lab1.validation.PersonValidator;
import org.vaskozov.is.lab1.websocket.ClientWebSocket;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PersonService {
    @Inject
    private ClientWebSocket clientWebSocket;

    @Inject
    private PersonRepository personRepository;

    @Inject
    private CoordinatesValidator coordinatesValidator;

    @Inject
    private LocationValidator locationValidator;

    @Inject
    private CoordinatesRepository coordinatesRepository;

    @Inject
    private LocationRepository locationRepository;

    @Inject
    private PersonValidator personValidator;

    @Inject
    private UserRepository userRepository;

    @Inject
    private OperationRepository operationRepository;

    @PersistenceContext(name = "Lab1PU")
    private EntityManager em;

    @Transactional
    public void delete(Long id) {
        var person = personRepository.findById(id);

        if (person.isEmpty()) {
            return;
        }

        personRepository.delete(person.get());
        clientWebSocket.broadcastPersonDeleted(person.get());
    }

    @Transactional
    public Result<Long, String> create(Person person) {
        var personValidationResult = personValidator.validate(person);

        if (personValidationResult.isError()) {
            return Result.error(personValidationResult.getError());
        }

        var coordinates = coordinatesRepository.save(person.getCoordinates());
        var location = locationRepository.save(person.getLocation());

        person.setCoordinates(coordinates);
        person.setLocation(location);
        person = personRepository.save(person);

        clientWebSocket.broadcastPersonUpdate(person);
        return Result.success(person.getId());
    }

    @Transactional
    public Result<Void, String> update(Person person) {
        Person existing = personRepository.findById(person.getId()).orElse(null);

        if (existing == null) {
            return Result.error("Person with id " + person.getId() + " not found");
        }

        if (person.getName() != null) {
            if (person.getName().isBlank()) {
                return Result.error("Name is required");
            }

            existing.setName(person.getName());
        }

        if (person.getCoordinates() != null) {
            var coordinatesValidationResult = coordinatesValidator.validate(person.getCoordinates());

            if (coordinatesValidationResult.isError()) {
                return coordinatesValidationResult;
            }

            updateCoordinates(existing, person.getCoordinates());
        }

        if (person.getLocation() != null) {
            var locationValidationResult = locationValidator.validate(person.getLocation());

            if (locationValidationResult.isError()) {
                return locationValidationResult;
            }

            updateLocation(existing, person.getLocation());
        }

        if (person.getHeight() != null) {
            if (person.getHeight() <= 0) {
                return Result.error("Height must be positive");
            }

            existing.setHeight(person.getHeight());
        }

        if (person.getWeight() != null) {
            if (person.getWeight() <= 0) {
                return Result.error("Weight must be positive");
            }

            existing.setWeight(person.getWeight());
        }

        if (person.getEyeColor() != null) {
            existing.setEyeColor(person.getEyeColor());
        }

        if (person.getHairColor() != null) {
            existing.setHairColor(person.getHairColor());
        }

        if (person.getNationality() != null) {
            existing.setNationality(person.getNationality());
        }

        coordinatesRepository.save(existing.getCoordinates());
        locationRepository.save(existing.getLocation());
        personRepository.save(existing);

        clientWebSocket.broadcastPersonUpdate(existing);

        return Result.success(null);
    }

    @Transactional
    public Result<Void, String> savePersons(List<Person> persons) {
        for (Person person : persons) {
            var personValidationResult = personValidator.validate(person);

            if (personValidationResult.isError()) {
                return Result.error(personValidationResult.getError());
            }
        }

        List<Person> savedPersons = new ArrayList<>();

        for (Person person : persons) {
            em.persist(person);
            person.setCreationTime(LocalDateTime.now());
            savedPersons.add(person);
        }

        persons = savedPersons;
        broadcastChangedPersons(persons);

        return Result.success(null);
    }

    public List<Person> getPersons() {
        return personRepository.findAll().toList();
    }

    public void broadcastChangedPersons(List<Person> persons) {
        List<Person> updatedPersons = new ArrayList<>();

        for (var p : persons) {
            if (p.getCoordinates() != null) {
                updatedPersons.addAll(personRepository.findByCoordinates(p.getCoordinates()));
            }

            if (p.getLocation() != null) {
                updatedPersons.addAll(personRepository.findByLocation(p.getLocation()));
            }
        }

        for (var p : persons) {
            clientWebSocket.broadcastPersonUpdate(p);
        }

        for (var p : updatedPersons) {
            clientWebSocket.broadcastPersonUpdate(p);
        }
    }

    private void updateCoordinates(Person person, Coordinates coordinates) {
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

    private void updateLocation(Person person, Location location) {
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
