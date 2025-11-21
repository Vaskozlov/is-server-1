package org.vaskozov.is.lab1.repository;

import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import org.vaskozov.is.lab1.bean.Coordinates;
import org.vaskozov.is.lab1.bean.Location;
import org.vaskozov.is.lab1.bean.Person;
import org.vaskozov.is.lab1.bean.User;

import java.util.List;

@Repository(dataStore = "Lab1PU")
public interface PersonRepository extends BasicRepository<Person, Long> {
    @Find
    List<Person> findByName(String name);

    @Find
    List<Person> findByCoordinates(Coordinates coordinates);

    @Find
    List<Person> findByLocation(Location location);
}
