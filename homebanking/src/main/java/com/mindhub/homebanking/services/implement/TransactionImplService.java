package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.TransactionRepository;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionImplService implements TransactionService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionImplService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    @Transactional
    public ResponseEntity<Object> performTransaction(Integer amount, String description, String sourceAccountNumber, String destinationAccountNumber, Authentication authentication) {
        Account sourceAccount = accountRepository.findByNumber(sourceAccountNumber);
        Account destinationAccount = accountRepository.findByNumber(destinationAccountNumber);

        ResponseEntity<Object> validationResponse = validateTransaction(sourceAccount, destinationAccount, authentication, amount);
        if (validationResponse != null) {
            return validationResponse;
        }

        performTransactionInternal(amount, description, sourceAccount, destinationAccount);
        return ResponseEntity.ok("Transaction successful");
    }

    private void performTransactionInternal(Integer amount, String description, Account sourceAccount, Account destinationAccount) {
        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, -amount, description, LocalDateTime.now(), sourceAccount);
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(), destinationAccount);

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);

        accountRepository.save(sourceAccount);
        accountRepository.save(destinationAccount);
    }
    private ResponseEntity<Object> validateTransaction(Account sourceAccount, Account destinationAccount, Authentication authentication, Integer amount) {
        if (sourceAccount == null) {
            return ResponseEntity.badRequest().body("Source account not found");
        }

        if (destinationAccount == null) {
            return ResponseEntity.badRequest().body("Destination account not found");
        }

        if (sourceAccount.getNumber().equals(destinationAccount.getNumber())) {
            return ResponseEntity.badRequest().body("Source and destination accounts must be different");
        }

        if (!sourceAccount.getClient().getEmail().equals(authentication.getName())) {
            return ResponseEntity.badRequest().body("Source account does not belong to authenticated user");
        }

        if (sourceAccount.getBalance() < amount) {
            return ResponseEntity.badRequest().body("Insufficient balance in source account");
        }

        return null;
    }
}
