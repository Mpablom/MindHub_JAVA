package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;


@RepositoryRestResource
public interface ClientRepository extends JpaRepository<Client,Long> {
    Optional<Client> findById(Long id);
    Client findByEmail(String email);
    Client save(Client client);
    List<Client> findAll();
}
