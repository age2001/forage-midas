package com.jpmc.midascore.component;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.foundation.Transaction;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class DatabaseConduit {
    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;

    public DatabaseConduit(UserRepository userRepository, TransactionRecordRepository transactionRecordRepository) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
    }

    public void save(UserRecord userRecord) {
        userRepository.save(userRecord);
    }
    public void save(Transaction transaction) {

        UserRecord sender = userRepository.findById(transaction.getSenderId());
        UserRecord recipient = userRepository.findById(transaction.getRecipientId());

        TransactionRecord transactionRecord = new TransactionRecord(
                sender,
                recipient,
                transaction.getAmount(),
                transaction.getIncentive()
        );
        transactionRecordRepository.save(transactionRecord);

        sender.setBalance(sender.getBalance() - transaction.getAmount());
        save(sender);
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + transaction.getIncentive());
        save(recipient);
    }

    public boolean transactionIsValid(Transaction transaction) {
        if (!userRepository.existsById(transaction.getSenderId())) {
            return false;
        }

        if (!userRepository.existsById(transaction.getRecipientId())) {
            return false;
        }

        if (userRepository.findById(transaction.getSenderId()).getBalance() < transaction.getAmount()) {
            return false;
        }

        return true;
    }

    public float queryBalance(Long userId) {
        if (userRepository.existsById(userId)) {
            return userRepository.findById(userId).get().getBalance();
        }

        return 0f;
    }
}
