package org.vaskozov.is.lab1.repository;

import jakarta.data.repository.BasicRepository;
import jakarta.data.repository.Find;
import jakarta.data.repository.Repository;
import org.vaskozov.is.lab1.bean.Operation;
import org.vaskozov.is.lab1.bean.OperationType;
import org.vaskozov.is.lab1.bean.User;

import java.util.List;

@Repository(dataStore = "Lab1PU")
public interface OperationRepository extends BasicRepository<Operation, Long> {
    @Find
    List<Operation> findByType(OperationType type);

    @Find
    List<Operation> findByUser(User user);
}
