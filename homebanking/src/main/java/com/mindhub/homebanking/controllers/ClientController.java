package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("/api")
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClients(){
        return clientRepository.findAll()
                .stream().map(ClientDTO::new).collect(toList());
    }

    @GetMapping("/clients/current")
    public ResponseEntity<ClientDTO> getClientAuth(Authentication authentication) {
        Optional<Client> clientOptional = Optional.ofNullable(clientRepository.findByEmail(authentication.getName()));

        ClientDTO clientDTO = new ClientDTO(clientOptional.get());
        return ResponseEntity.ok(clientDTO);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable Long id){
        Optional<ClientDTO> clientDTOOptional = clientRepository.findById(id)
                .map(ClientDTO::new);

        return clientDTOOptional
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(path = "/clients")
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


        if (clientRepository.findByEmail(email) !=  null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }

        clientRepository.save(new Client(firstName, lastName, email, passwordEncoder.encode(password)));
        return new ResponseEntity<>("Client successfully created",HttpStatus.CREATED);
    }
}
