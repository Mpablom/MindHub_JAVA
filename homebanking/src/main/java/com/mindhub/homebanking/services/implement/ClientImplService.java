package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.time.LocalDate;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class ClientImplService implements ClientService {
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    @Autowired
    public ClientImplService(ClientRepository clientRepository, PasswordEncoder passwordEncoder, AccountRepository accountRepository, AccountService accountService) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @Override
    public List<ClientDTO> getClients(){
        return clientRepository.findAll().stream().map(ClientDTO::new).collect(toList());
    }

    @Override
    public ClientDTO getClientAuth(Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());
        return new ClientDTO(client);
    }

    @Override
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id){
        return clientRepository.findById(id)
                .map(ClientDTO::new)
                .map(clientDTO -> ResponseEntity.ok().body(clientDTO))
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password){
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

        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        Client client= new Client(firstName, lastName, email, passwordEncoder.encode(password));
        clientRepository.save(client);

        LocalDate today = LocalDate.now();
        String accountNumber = accountService.generateAccountNumber();
        Account newAccount = new Account(accountNumber, today, 0.0, client);
        accountRepository.save(newAccount);

        return new ResponseEntity<>("Client successfully created",HttpStatus.CREATED);
    }
}