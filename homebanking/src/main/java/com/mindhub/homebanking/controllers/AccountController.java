package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.TransactionService;
import com.mindhub.homebanking.utils.AccountUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountService accountService;
    private final ClientService clientService;
    private final TransactionService transactionService;

    @Autowired
    public AccountController(AccountService accountService, ClientService clientService, TransactionService transactionService) {
        this.accountService = accountService;
        this.clientService = clientService;
        this.transactionService = transactionService;
    }

    @GetMapping("/accounts")
    public List<AccountDTO> getAccounts(){
        return accountService.findAll().stream()
                .map(AccountDTO::new).collect(Collectors.toList());
    }
    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable Long id, Authentication authentication) {
        Optional<Account> optionalAccount = accountService.findById(id);

        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            Client authenticatedClient = clientService.findByEmail(authentication.getName());

            if (account.getClient().equals(authenticatedClient)) {
                // Filtrar las transacciones activas
                List<Transaction> activeTransactions = new ArrayList<>();
                for (Transaction transaction : account.getTransactions()) {
                    if (transaction.isActive()) {
                        activeTransactions.add(transaction);
                    }
                }

                // Asignar las transacciones activas a la cuenta
                account.setTransactions(activeTransactions);

                AccountDTO accountDTO = new AccountDTO(account);
                return ResponseEntity.ok(accountDTO);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/clients/current/accounts")
    public ResponseEntity<Object> createAccount(Authentication authentication) {
        try {
            // Obtener el cliente actual
            Client client = clientService.findByEmail(authentication.getName());

            // Verificar si el cliente ya tiene 3 cuentas activas
            if (client.getActiveAccountCount() > 3) {
                return new ResponseEntity<>("You have reached the maximum limit of active accounts.", HttpStatus.BAD_REQUEST);
            }

            // Crear la nueva cuenta y asignarla al cliente
            Account account = new Account();
            account.setClient(client);

            // Configurar otros datos de la cuenta, como número, saldo inicial, etc.
            // Por ejemplo:
            account.setNumber(AccountUtils.generateAccountNumber()); // Genera un número de cuenta único
            account.setBalance(0.0); // Saldo inicial

            // Guardar la cuenta y actualizar el contador de cuentas activas
            accountService.save(account);
            clientService.incrementActiveAccountCount(client);
            clientService.save(client);

            return new ResponseEntity<>("Account created successfully.", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating account: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getClientAccounts(Authentication authentication) {
        Client client = clientService.findByEmail(authentication.getName());

        List<Account> clientAccounts = new ArrayList<>(client.getAccounts());

        // Filtrar solo las cuentas activas
        List<AccountDTO> accountDTOs = clientAccounts.stream()
                .filter(Account::isActive)
                .map(AccountDTO::new)
                .collect(Collectors.toList());

        return accountDTOs;
    }
    @DeleteMapping("/accounts/{id}")
    public ResponseEntity<String> desactivateAccount(@PathVariable Long id) {
        try {
            Optional<Account> accountOptional = accountService.findById(id);
            if (accountOptional.isPresent()) {
                Account account = accountOptional.get();

                // Verificar si la cuenta tiene saldo y otras restricciones antes de desactivarla
                if (account.getBalance() > 0) {
                    return new ResponseEntity<>("No se puede desactivar una cuenta con saldo positivo", HttpStatus.BAD_REQUEST);
                }

                // Verificar si es la única cuenta activa del cliente
                if (account.isActive() && account.getClient().getActiveAccounts().size() == 1) {
                    return new ResponseEntity<>("No se puede desactivar la única cuenta activa del cliente", HttpStatus.BAD_REQUEST);
                }

                if (account.isActive()) {
                    // Desactivar lógicamente las transacciones asociadas
                    for (Transaction transaction : account.getTransactions()) {
                        transaction.setActive(false);
                        transactionService.save(transaction);
                    }

                    // Restar la cuenta activa del cliente
                    Client client = account.getClient();
                    clientService.decrementActiveAccountCount(client);

                    // Desactivar la cuenta y guardarla
                    account.setActive(false);
                    accountService.save(account);

                    return new ResponseEntity<>("Cuenta desactivada exitosamente", HttpStatus.OK);
                } else {
                    return new ResponseEntity<>("La cuenta ya está desactivada", HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>("Cuenta no encontrada", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error al desactivar la cuenta: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
