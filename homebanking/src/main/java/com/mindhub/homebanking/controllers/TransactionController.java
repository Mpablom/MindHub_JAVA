package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountService accountService;

    @Autowired
    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @PostMapping("/transactions")
    public ResponseEntity<Object> performTransaction(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam Double amount,
            @RequestParam String description,
            Authentication authentication) {

        if (fromAccountNumber.equals("") || toAccountNumber.equals("") || description.equals("")) {
            return ResponseEntity.badRequest().body("Missing or invalid parameters");
        }
        Account sourceAccount = accountService.findByNumber(fromAccountNumber);
        Account destinationAccount = accountService.findByNumber(toAccountNumber);

        ResponseEntity<Object> validationResponse = validateTransaction(sourceAccount, destinationAccount, authentication, amount);
        if (validationResponse != null) {
            return validationResponse;
        }

        Transaction debitTransaction = new Transaction(TransactionType.DEBIT, -amount, description, LocalDateTime.now(), sourceAccount,sourceAccount.getBalance()-amount, true);
        Transaction creditTransaction = new Transaction(TransactionType.CREDIT, amount, description, LocalDateTime.now(), destinationAccount,destinationAccount.getBalance()+amount, true);

        sourceAccount.setBalance(sourceAccount.getBalance() - amount);
        destinationAccount.setBalance(destinationAccount.getBalance() + amount);

        transactionService.save(debitTransaction);
        transactionService.save(creditTransaction);

        accountService.save(sourceAccount);
        accountService.save(destinationAccount);
        return ResponseEntity.ok("Transaction successful");
    }

    private ResponseEntity<Object> validateTransaction(Account sourceAccount, Account destinationAccount, Authentication authentication, Double amount) {
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

