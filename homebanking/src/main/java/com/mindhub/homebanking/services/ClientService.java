package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

public interface ClientService {
    Optional<Client> findById(Long id);
    Client findByEmail(String email);
    Client save(Client client);
    List<Client> findAll();
    void incrementActiveAccountCount(Client client);
    void decrementActiveAccountCount(Client client);
    int getActiveAccountCount(Client client);
}
