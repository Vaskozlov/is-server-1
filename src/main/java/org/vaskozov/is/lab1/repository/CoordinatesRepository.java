package org.vaskozov.is.lab1.repository;

import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Repository;
import org.vaskozov.is.lab1.bean.Coordinates;

@Repository(dataStore = "Lab1PU")
public interface CoordinatesRepository extends BasicRepository<Coordinates, Long> {
}
