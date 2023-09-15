package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;


@RestController
@RequestMapping("/api")
public class ClientController {
    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    @Autowired
    public ClientController(ClientService clientService, PasswordEncoder passwordEncoder, AccountService accountService) {
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
    }

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientService.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("/clients/current")
    public ClientDTO getClientAuth(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id){
       return clientService.findById(id).map(ClientDTO::new)
               .map(clientDTO -> ResponseEntity.ok().body(clientDTO))
               .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/clients")
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password) {
        if (firstName.isEmpty()){
            return new ResponseEntity<>("Missing data, name cannot be empty", HttpStatus.FORBIDDEN);
        }

        if (lastName.isEmpty()){
            return new ResponseEntity<>("Missing data, last name cannot be empty", HttpStatus.FORBIDDEN);
        }

        if (email.isEmpty()){
            return new ResponseEntity<>("Missing data, email cannot be empty", HttpStatus.FORBIDDEN);
        }else if(!email.contains("@")){
            return new ResponseEntity<>("Missing data, email must have @", HttpStatus.FORBIDDEN);
        }
        if (password.isEmpty()){
            return new ResponseEntity<>("Missing data, password cannot be empty", HttpStatus.FORBIDDEN);
        } else if (password.length()<4) {
            return new ResponseEntity<>("Missing data,password must be at least 4 characters long", HttpStatus.FORBIDDEN);
        }

        if (clientService.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client= new Client(firstName, lastName, email, passwordEncoder.encode(password),1);
        clientService.save(client);

        LocalDate today = LocalDate.now();
        String accountNumber = AccountUtils.generateAccountNumber();
        while (accountService.existsByNumber(accountNumber)){
            accountNumber = AccountUtils.generateAccountNumber();
        }
        Account newAccount = new Account(accountNumber, today, 0.0, client,true);
        accountService.save(newAccount);

        return new ResponseEntity<>("Client successfully created",HttpStatus.CREATED);
    }
}
