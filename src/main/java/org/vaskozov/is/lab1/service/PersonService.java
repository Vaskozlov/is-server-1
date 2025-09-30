package org.vaskozov.is.lab1.service;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.websocket.ClientWebSocket;

import java.util.List;

@ApplicationScoped
public class PersonService {
    @Inject
    private ClientWebSocket clientWebSocket;

    @EJB(name = "java:global/is_lab_1/PersonsDatabase")
    private PersonsDatabaseInterface database;

    public Person create(Person person) {
        database.savePerson(person);
        clientWebSocket.broadcastPerson(person);
        return person;
    }

    public Person update(Person person) {
        person = database.updatePerson(person);
        clientWebSocket.broadcastPerson(person);
        return person;
    }

    public List<Person> getPersons() {
        return database.getPersons();
    }
}
