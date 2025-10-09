package org.vaskozov.is.lab1.service;

import jakarta.ejb.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.vaskozov.is.lab1.bean.Coordinates;
import org.vaskozov.is.lab1.bean.Location;
import org.vaskozov.is.lab1.bean.Person;

import java.util.List;
import java.util.Optional;

@Singleton
public class PersonsDatabase implements PersonsDatabaseInterface {
    @PersistenceContext(name = "Lab1PU")
    private EntityManager em;

    @Override
    public Person savePerson(Person person) {
        if (person.getId() == null) {
            person = em.merge(person);
            return person;
        }

        return null;
    }

    @Override
    public Optional<Person> getPerson(long id) {
        return Optional.ofNullable(em.find(Person.class, id));
    }

    @Override
    public boolean deletePerson(long id) {
        try {
            Person p = em.find(Person.class, id);
            em.remove(p);
            return true;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    @Override
    public List<Person> getPersons() {
        return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    @Override
    public List<Person> getPersonsByCoordinateId(long id) {
        return em
                .createQuery("SELECT p FROM Person p WHERE p.coordinates.id = :id", Person.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public List<Person> getPersonsByLocationId(long id) {
        return em
                .createQuery("SELECT p FROM Person p WHERE p.location.id = :id", Person.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Override
    public Person updatePerson(Person updatedPerson) {
        Person existing = em.find(Person.class, updatedPerson.getId());

        if (existing == null) {
            throw new IllegalArgumentException("Person with id " + updatedPerson.getId() + " not found");
        }

        if (updatedPerson.getName() != null) {
            existing.setName(updatedPerson.getName());
        }

        if (updatedPerson.getEyeColor() != null) {
            existing.setEyeColor(updatedPerson.getEyeColor());
        }

        if (updatedPerson.getHairColor() != null) {
            existing.setHairColor(updatedPerson.getHairColor());
        }

        if (updatedPerson.getHeight() != null) {
            existing.setHeight(updatedPerson.getHeight());
        }

        if (updatedPerson.getWeight() != null) {
            existing.setWeight(updatedPerson.getWeight());
        }

        if (updatedPerson.getNationality() != null) {
            existing.setNationality(updatedPerson.getNationality());
        }

        if (updatedPerson.getCoordinates() != null) {
            updateCoordinates(updatedPerson, existing);
        }

        if (updatedPerson.getLocation() != null) {
            updateLocation(updatedPerson, existing);
        }

        return existing;
    }

    private void updateLocation(Person updatedPerson, Person existing) {
        var locId = updatedPerson.getLocation().getId();

        if (locId == null) {
            var newLocation = new Location();
            newLocation.setX(updatedPerson.getLocation().getX());
            newLocation.setY(updatedPerson.getLocation().getY());
            newLocation.setName(updatedPerson.getLocation().getName());
            existing.setLocation(newLocation);
            return;
        }

        var location = em.find(Location.class, updatedPerson.getLocation().getId());
        var newLocation = updatedPerson.getLocation();

        location.setX(newLocation.getX());
        location.setY(newLocation.getY());
        location.setName(newLocation.getName());

        existing.setLocation(location);
    }

    private void updateCoordinates(Person updatedPerson, Person existing) {
        var coordsId = updatedPerson.getCoordinates().getId();

        if (coordsId == null) {
            var newCoordinates = new Coordinates();
            newCoordinates.setX(updatedPerson.getCoordinates().getX());
            newCoordinates.setY(updatedPerson.getCoordinates().getY());
            existing.setCoordinates(newCoordinates);
            return;
        }

        var coords = em.find(Coordinates.class, updatedPerson.getCoordinates().getId());
        var newCoords = updatedPerson.getCoordinates();

        coords.setX(newCoords.getX());
        coords.setY(newCoords.getY());

        existing.setCoordinates(coords);
    }

    @Override
    public boolean hasLocation(long id){
        return em.find(Location.class, id) != null;
    }

    @Override
    public boolean hasPerson(long id)
    {
        return em.find(Person.class, id) != null;
    }

    @Override
    public boolean hasCoordinate(long id)
    {
        return em.find(Coordinates.class, id) != null;
    }
}
