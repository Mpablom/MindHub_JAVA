package com.mindhub.homebanking.services;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface TransactionService {
   ResponseEntity<Object> performTransaction(Double amount, String description, String sourceAccountNumber, String destinationAccountNumber, Authentication authentication);
}