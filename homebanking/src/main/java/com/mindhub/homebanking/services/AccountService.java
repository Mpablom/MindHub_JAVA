package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.AccountDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface AccountService {
    List<AccountDTO> getAccounts();
    ResponseEntity<AccountDTO> getAccount(Long id, Authentication authentication);
    ResponseEntity<Object> createAccount(Authentication authentication);
    String generateAccountNumber();
    ResponseEntity<String> performTransaction(Integer amount, String description, String sourceAccountNumber, String destinationAccountNumber, Authentication authentication);
    List<AccountDTO> getClientAccounts(String clientEmail);
}
