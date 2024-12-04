package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionVerifier {
    private final DatabaseConduit databaseConduit;
    private final IncentiveApiQuery incentiveApiQuery;

    public TransactionVerifier(DatabaseConduit databaseConduit, IncentiveApiQuery incentiveApiQuery) {
        this.databaseConduit = databaseConduit;
        this.incentiveApiQuery = incentiveApiQuery;
    }

    public void verifyTransaction(Transaction transaction) {
        if (databaseConduit.transactionIsValid(transaction)) {
            transaction.setIncentive(incentiveApiQuery.fetchIncentiveFromApi(transaction).getAmount());
            databaseConduit.save(transaction);
        }
    }
}
