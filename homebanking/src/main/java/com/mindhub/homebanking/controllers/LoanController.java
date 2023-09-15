package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.DTO.LoanApplicationDTO;
import com.mindhub.homebanking.DTO.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class LoanController {

    private final LoanService loanService;
    private final ClientService clientService;
    private final AccountService accountService;
    private final ClientLoanService clientLoanService;
    private final TransactionService transactionService;

    @Autowired
    public LoanController(LoanService loanService, ClientService clientService, AccountService accountService, ClientLoanService clientLoanService, TransactionService transactionService) {
        this.loanService = loanService;
        this.clientService = clientService;
        this.accountService = accountService;
        this.clientLoanService = clientLoanService;
        this.transactionService = transactionService;
    }

    @PostMapping("/loans")
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        ResponseEntity<Object> validationResponse = validateLoan(loanApplicationDTO);
        if (validationResponse != null) {
            return validationResponse;
        }

        Optional<Loan> loanOptional = loanService.findById(loanApplicationDTO.getLoanId());
        if (!loanOptional.isPresent()) {
            return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
        }
        Loan loan = loanOptional.get();
        ClientDTO authenticatedClientDTO = new ClientDTO(clientService.findByEmail(authentication.getName()));
        Account account = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());

        double loanAmount = loanApplicationDTO.getAmount();
        double totalDebt = loanAmount * 1.20; // Deuda total incluyendo intereses

        ClientLoan clientLoan = new ClientLoan(totalDebt, Collections.singletonList(loanApplicationDTO.getPayments()));

        // Obtener el cliente autenticado
        Client authenticatedClient = clientService.findById(authenticatedClientDTO.getId()).orElse(null);

        if (authenticatedClient != null) {
            clientLoan.setClient(authenticatedClient);
        } else {
            return new ResponseEntity<>("Invalid client", HttpStatus.BAD_REQUEST);
        }

        clientLoan.setLoan(loan);

        clientLoanService.save(clientLoan);

        String transactionDescription = loan.getName() + " loan approved";
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanAmount, transactionDescription, LocalDateTime.now(), account,account.getBalance()+loanApplicationDTO.getAmount(), true);
        transactionService.save(transaction);

        // Actualizar el saldo de la cuenta
        account.setBalance(account.getBalance() + loanAmount);
        accountService.save(account);
        return new ResponseEntity<>("Loan application successful", HttpStatus.CREATED);
    }

    @GetMapping("/loans")
    public ResponseEntity<List<LoanDTO>> getAvailableLoans() {
        List<LoanDTO> availableLoans = loanService.getLoans();
        return new ResponseEntity<>(availableLoans, HttpStatus.OK);
    }
    private ResponseEntity<Object> validateLoan(LoanApplicationDTO loanApplicationDTO) {
        if (loanApplicationDTO.getAmount() <= 0 ||
                loanApplicationDTO.getPayments() == null ||
                loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Invalid loan application data", HttpStatus.BAD_REQUEST);
        }

        Loan loan = loanService.findById(loanApplicationDTO.getLoanId()).orElse(null);

        if (loan == null) {
            return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
        }

        if (loanApplicationDTO.getAmount() > loan.getMaxAmount()) {
            return new ResponseEntity<>("Loan amount exceeds maximum allowed", HttpStatus.BAD_REQUEST);
        }

        List<Integer> availablePayments = loan.getPayments();

        if (loanApplicationDTO.getPayments() == null || !availablePayments.contains(loanApplicationDTO.getPayments())) {
            return new ResponseEntity<>("Invalid loan payment terms", HttpStatus.BAD_REQUEST);
        }

        Account account = accountService.findByNumber(loanApplicationDTO.getToAccountNumber());

        if (account == null) {
            return new ResponseEntity<>("Destination account not found", HttpStatus.NOT_FOUND);
        }

        return null;
    }
}
