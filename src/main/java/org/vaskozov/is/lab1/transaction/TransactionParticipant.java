package org.vaskozov.is.lab1.transaction;

public interface TransactionParticipant {
    void prepare() throws Exception;

    void commit() throws Exception;

    void rollback() throws Exception;
}
