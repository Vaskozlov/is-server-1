package org.vaskozov.is.lab1.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class DistributedTransactionCoordinator {
    private final List<TransactionParticipant> participants = new ArrayList<>();

    public void addParticipant(TransactionParticipant participant) {
        participants.add(participant);
    }

    public <T> T coordinate(Supplier<T> onSuccess) throws Exception {
        System.out.println("Starting 2PC prepare phase");

        for (TransactionParticipant p : participants) {
            try {
                p.prepare();
            } catch (Exception e) {
                System.err.println("Prepare failed for participant: " + p.getClass().getSimpleName() + " - " + e.getMessage());
                rollbackAll();
                throw e;
            }
        }

        System.out.println("Prepare successful, starting commit phase");
        try {
            T result = onSuccess.get();
            for (TransactionParticipant p : participants) {
                p.commit();
            }
            System.out.println("Commit successful");
            return result;
        } catch (Exception e) {
            System.err.println("Commit failed: " + e.getMessage());
            rollbackAll();
            throw e;
        }
    }

    private void rollbackAll() {
        System.out.println("Starting rollback");
        for (TransactionParticipant p : participants) {
            try {
                p.rollback();
            } catch (Exception e) {
                System.err.println("Rollback failed for participant: " + p.getClass().getSimpleName() + " - " + e.getMessage());
            }
        }
        System.out.println("Rollback completed");
    }
}