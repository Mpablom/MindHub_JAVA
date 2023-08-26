package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    ClientRepository clientRepository;

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream()
                .map(AccountDTO::new).collect(Collectors.toList());
    }
    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id){
        return accountRepository.findById(id)
                .map(AccountDTO::new)
                .map(ResponseEntity::ok)
                .orElse(null);
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        LocalDate today = LocalDate.now();

        Client client = clientRepository.findByEmail(authentication.getName());

        if (client == null) {
            return new ResponseEntity<>("Client not found", HttpStatus.NOT_FOUND);
        }
        if (client.getAccounts().size() >= 3) {
            return new ResponseEntity<>("Maximum account limit reached", HttpStatus.FORBIDDEN);
        }

        String accountNumber = generateAccountNumber();
        Account newAccount = new Account(accountNumber,today,0.0,client);
        accountRepository.save(newAccount);

        return new ResponseEntity<>("Account successfully created", HttpStatus.CREATED);
    }

    //genera los números aleatorios de la cuenta

    public String generateAccountNumber(){
        StringBuilder accountNumber = new StringBuilder("VIN-");
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int randomNumber = random.nextInt(10);
            accountNumber.append(randomNumber);
        }

        return accountNumber.toString();
    }
}
