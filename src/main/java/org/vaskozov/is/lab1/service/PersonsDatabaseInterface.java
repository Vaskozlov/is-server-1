package org.vaskozov.is.lab1.service;

import org.vaskozov.is.lab1.bean.Person;

import java.util.List;
import java.util.Optional;

public interface PersonsDatabaseInterface {
    boolean savePerson(Person person);

    Optional<Person> getPerson(long id);

    boolean deletePerson(long id);

    List<Person> getPersons();

    Person updatePerson(Person person);
}
