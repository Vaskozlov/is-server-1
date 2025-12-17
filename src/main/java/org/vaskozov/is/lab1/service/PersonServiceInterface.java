package org.vaskozov.is.lab1.service;

import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.util.Result;

import java.util.List;

public interface PersonServiceInterface {
    List<Person> getPersons();

    Result<Person, String> create(Person person);

    Result<List<Person>, String> create(List<Person> person);

    Result<Person, String> update(Person person);

    void delete(Long id);
}
