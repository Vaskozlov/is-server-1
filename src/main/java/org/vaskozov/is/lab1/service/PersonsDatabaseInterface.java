package org.vaskozov.is.lab1.service;

import org.vaskozov.is.lab1.bean.Person;

import java.util.List;
import java.util.Optional;

public interface PersonsDatabaseInterface {
    Person savePerson(Person person);

    Optional<Person> getPerson(long id);

    boolean deletePerson(long id);

    List<Person> getPersons();

    List<Person> getPersonsByCoordinateId(long id);

    List<Person> getPersonsByLocationId(long id);

    Person updatePerson(Person person);

    boolean hasLocation(long id);

    boolean hasPerson(long id);

    boolean hasCoordinate(long id);
}
