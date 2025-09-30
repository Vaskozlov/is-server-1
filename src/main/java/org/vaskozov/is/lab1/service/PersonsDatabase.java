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
    public boolean savePerson(Person person) {
        try {
            if (person.getId() == null) {
                em.persist(person);
                return true;
            }
        } catch (Exception ignored) {

        }

        return false;
    }

    @Override
    public Optional<Person> getPerson(long id) {
        return Optional.ofNullable(em.find(Person.class, id));
    }

    @Override
    public boolean deletePerson(long id) {
        return false;
    }

    @Override
    public List<Person> getPersons() {
        return em.createQuery("SELECT p FROM Person p", Person.class).getResultList();
    }

    @Override
    public Person updatePerson(Person updatedPerson) {
        Person existing = em.find(Person.class, updatedPerson.getId());
        Coordinates coordinates = em.find(Coordinates.class, updatedPerson.getCoordinates().getId());
        Location location = em.find(Location.class, updatedPerson.getLocation().getId());

        if (existing == null) {
            throw new IllegalArgumentException("Person with id " + updatedPerson.getId() + " not found");
        }

        existing.setName(updatedPerson.getName());
        existing.setEyeColor(updatedPerson.getEyeColor());
        existing.setHairColor(updatedPerson.getHairColor());
        existing.setHeight(updatedPerson.getHeight());
        existing.setWeight(updatedPerson.getWeight());
        existing.setNationality(updatedPerson.getNationality());

        if (existing.getCoordinates().getId().equals(coordinates.getId())) {
            coordinates.setX(updatedPerson.getCoordinates().getX());
            coordinates.setY(updatedPerson.getCoordinates().getY());
        } else {
            existing.getCoordinates().setId(coordinates.getId());
        }

        if (existing.getLocation().getId().equals(location.getId())) {
            location.setX(updatedPerson.getLocation().getX());
            location.setY(updatedPerson.getLocation().getY());
            location.setName(updatedPerson.getLocation().getName());
        } else {
            existing.getLocation().setId(location.getId());
        }

        return existing;
    }
}
