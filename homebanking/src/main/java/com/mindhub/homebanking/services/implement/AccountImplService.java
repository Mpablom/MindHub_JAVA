package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountImplService implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id){
        return accountRepository.findById(id)
                .map(AccountDTO::new)
                .map(ResponseEntity::ok)
                .orElse(null);
    }

    @Override
    public List<AccountDTO> getAccounts(){
        return accountRepository.findAll().stream()
                .map(AccountDTO::new).collect(Collectors.toList());
    }

    @Override
    public ResponseEntity<Object> createAccount(Authentication authentication){
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

    @Override
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
