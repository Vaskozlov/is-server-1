package org.vaskozov.is.lab1.repository;

import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import org.vaskozov.is.lab1.bean.User;

import java.util.Optional;

@Repository(dataStore = "Lab1PU")
public interface UserRepository extends BasicRepository<User, Long> {
    @Find
    Optional<User> findByUsername(String username);
}
