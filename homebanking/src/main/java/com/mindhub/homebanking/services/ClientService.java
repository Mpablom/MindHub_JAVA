package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.ClientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ClientService {
    List<ClientDTO> getClients();
    ClientDTO getClientAuth(Authentication authentication);
    ResponseEntity<ClientDTO> getClient(@PathVariable Long id);
    ResponseEntity<Object> register(
            @RequestParam String firstName, @RequestParam String lastName,
            @RequestParam String email, @RequestParam String password);
}
