package org.vaskozov.is.lab1.service;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.websocket.ClientWebSocket;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PersonService {
    @Inject
    private ClientWebSocket clientWebSocket;

    @EJB(name = "java:global/is_lab_1/PersonsDatabase")
    private PersonsDatabaseInterface database;

    public void delete(Person person) {
        if (database.deletePerson(person.getId())) {
            clientWebSocket.broadcastPersonDeleted(person);
        }
    }

    public Person create(Person person) {
        person = database.savePerson(person);

        if (person != null) {
            clientWebSocket.broadcastPersonUpdate(person);
        }

        return person;
    }

    public Person update(Person person) {
        person = database.updatePerson(person);

        List<Person> updatedPersons = new ArrayList<>();
        updatedPersons.add(person);

        if (person.getCoordinates() != null) {
            updatedPersons.addAll(database.getPersonsByCoordinateId(person.getCoordinates().getId()));
        }

        if (person.getLocation() != null) {
            updatedPersons.addAll(database.getPersonsByLocationId(person.getLocation().getId()));
        }

        System.out.println(updatedPersons);

        for (var p : updatedPersons) {
            clientWebSocket.broadcastPersonUpdate(p);
        }

        return person;
    }

    public List<Person> getPersons() {
        return database.getPersons();
    }
}
