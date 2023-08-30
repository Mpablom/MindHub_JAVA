package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class AccountImplService implements AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ClientRepository clientRepository;
    private TransactionService transactionService;

    @Autowired
    public void setTransactionService(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

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

    @Transactional
    public ResponseEntity<String> performTransaction(Integer amount, String description, String sourceAccountNumber, String destinationAccountNumber, Authentication authentication) {
        Account sourceAccount = accountRepository.findByNumber(sourceAccountNumber);
        Account destinationAccount = accountRepository.findByNumber(destinationAccountNumber);

        if (sourceAccount == null || destinationAccount == null) {
            return ResponseEntity.badRequest().body("Source or destination account not found");
        }

        if (sourceAccountNumber.equals(destinationAccountNumber)) {
            return ResponseEntity.badRequest().body("Source and destination accounts must be different");
        }

        if (!sourceAccount.getClient().getEmail().equals(authentication.getName())) {
            return ResponseEntity.badRequest().body("Source account does not belong to authenticated user");
        }

        if (sourceAccount.getBalance() < amount) {
            return ResponseEntity.badRequest().body("Insufficient balance in source account");
        }

        try {
            transactionService.performTransaction(amount, description, sourceAccount.getNumber(), destinationAccount.getNumber(), authentication);
            return ResponseEntity.ok("Transaction successful");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public List<AccountDTO> getClientAccounts(String clientEmail) {
        Client client = clientRepository.findByEmail(clientEmail);

        List<Account> clientAccounts = new ArrayList<>(client.getAccounts());
        List<AccountDTO> accountDTOs = clientAccounts.stream()
                .map(AccountDTO::new)
                .collect(Collectors.toList());

        return accountDTOs;
    }
}
