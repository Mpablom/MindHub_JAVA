package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;
import java.util.Optional;

public interface AccountService {
    ResponseEntity<String> performTransaction(Double amount, String description, String sourceAccountNumber, String destinationAccountNumber, Authentication authentication);
    Account findByNumber(String accountNumber);
    Account save(Account account);
    Optional<Account> findById(Long id);
    boolean existsByNumber(String accountNumber);
    List<Account> findAll();
}
