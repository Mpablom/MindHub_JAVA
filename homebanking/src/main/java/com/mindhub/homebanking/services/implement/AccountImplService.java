package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.models.TransactionType;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountImplService implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;

    public AccountImplService(AccountRepository accountRepository,TransactionService transactionService){
        this.accountRepository=accountRepository;
        this.transactionService=transactionService;
    }

    @Override
    public Optional<Account> findById(Long id){
        return accountRepository.findById(id);
    }
    @Override
    public List<Account> findAll(){
        return accountRepository.findAll();
    }

    @Transactional
    public ResponseEntity<String> performTransaction(Double amount, String description, String sourceAccountNumber, String destinationAccountNumber, Authentication authentication) {
        Account sourceAccount = accountRepository.findByNumber(sourceAccountNumber);
        Account destinationAccount = accountRepository.findByNumber(destinationAccountNumber);

        if (sourceAccount == null || destinationAccount == null) {
            return ResponseEntity.badRequest().body("Source or destination account not found");
        }

        if (sourceAccountNumber.equals(destinationAccountNumber)) {
            return ResponseEntity.badRequest().body("Source and destination accounts must be different");
        }

        if (!sourceAccount.getClient().getEmail().equals(authentication.getName())) {
            return ResponseEntity.badRequest().body("Source account does not belong to authenticated user");
        }

        if (sourceAccount.getBalance() < amount) {
            return ResponseEntity.badRequest().body("Insufficient balance in source account");
        }

        try {
            performTransaction(amount, description, sourceAccount.getNumber(), destinationAccount.getNumber(), authentication);
            return ResponseEntity.ok("Transaction successful");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Override
    public Account findByNumber(String accountNumber){
        return accountRepository.findByNumber(accountNumber);
    }
    @Override
    public Account save(Account account){
        return accountRepository.save(account);
    }

    @Override
    public boolean existsByNumber(String accountNumber){
        return accountRepository.existsByNumber(accountNumber);
    }

}