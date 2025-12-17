package org.vaskozov.is.lab1.transaction;

import jakarta.transaction.UserTransaction;
import java.util.function.Supplier;

public class DbTransactionParticipant<T> implements TransactionParticipant {
    private final UserTransaction userTransaction;
    private final Supplier<T> dbOperation;

    private T result;

    public DbTransactionParticipant(UserTransaction userTransaction, Supplier<T> dbOperation) {
        this.userTransaction = userTransaction;
        this.dbOperation = dbOperation;
    }

    @Override
    public void prepare() throws Exception {
        userTransaction.begin();
        result = dbOperation.get();
    }

    @Override
    public void commit() throws Exception {
        userTransaction.commit();
    }

    @Override
    public void rollback() throws Exception {
        userTransaction.rollback();
    }

    public T getResult() {
        return result;
    }
}