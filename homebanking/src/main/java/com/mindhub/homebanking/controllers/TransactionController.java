package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.services.implement.AccountImplService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactions")
    public ResponseEntity<Object> performTransaction(
            @RequestParam String fromAccountNumber,
            @RequestParam String toAccountNumber,
            @RequestParam Integer amount,
            @RequestParam String description,
            Authentication authentication) {

        if (fromAccountNumber.equals("") || toAccountNumber.equals("") || description.equals("")) {
            return ResponseEntity.badRequest().body("Missing or invalid parameters");
        }

        ResponseEntity<Object> response = transactionService.performTransaction(amount, description, fromAccountNumber, toAccountNumber, authentication);

        if (response.getStatusCodeValue() == 200) {
            return ResponseEntity.ok("Transaction successful");
        } else {
            return ResponseEntity.status(response.getStatusCodeValue()).body(response.getBody());
        }
    }
}

