package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class ClientImplService implements ClientService {
    private final ClientRepository clientRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public ClientImplService(ClientRepository clientRepository, AccountRepository accountRepository) {
        this.clientRepository = clientRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public List<Client> findAll(){
        return clientRepository.findAll();
    }

    @Override
    public Optional<Client> findById(Long id){
        return clientRepository.findById(id);
    }

    @Override
    public Client findByEmail(String email){
        return clientRepository.findByEmail(email);
    }
    @Override
    public Client save(Client client){
        return clientRepository.save(client);
    }
    @Override
    public void incrementActiveAccountCount(Client client) {
        client.setActiveAccountCount(client.getActiveAccountCount() + 1);
    }
    @Override
    public void decrementActiveAccountCount(Client client) {
        client.setActiveAccountCount(client.getActiveAccountCount() - 1);
    }
    @Override
    public int getActiveAccountCount(Client client) {
        int activeAccountCount = accountRepository.countActiveAccountsByClientAndActiveIsTrue(client);
        return activeAccountCount;
    }
}