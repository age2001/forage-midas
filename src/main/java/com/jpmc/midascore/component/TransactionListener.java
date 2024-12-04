package com.jpmc.midascore.component;

import com.jpmc.midascore.foundation.Transaction;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TransactionListener {
    private final TransactionVerifier transactionVerifier;

    public TransactionListener(TransactionVerifier transactionVerifier) {
        this.transactionVerifier = transactionVerifier;
    }

    @KafkaListener(topics = "${general.kafka-topic}")
    public void receive(Transaction transaction) {
        transactionVerifier.verifyTransaction(transaction);
    }
}
