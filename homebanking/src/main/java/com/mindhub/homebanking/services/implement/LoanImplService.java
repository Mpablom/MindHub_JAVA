package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.DTO.ClientDTO;
import com.mindhub.homebanking.DTO.LoanApplicationDTO;
import com.mindhub.homebanking.DTO.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




@Service
public class LoanImplService implements LoanService {

    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    private final ClientService clientService;
    private final ClientRepository clientRepository;
    private final ClientLoanRepository clientLoanRepository;
    private final TransactionRepository transactionRepository;

    public LoanImplService(LoanRepository loanRepository, AccountRepository accountRepository, ClientService clientService, ClientRepository clientRepository, ClientLoanRepository clientLoanRepository, TransactionRepository transactionRepository) {
        this.loanRepository = loanRepository;
        this.accountRepository = accountRepository;
        this.clientService = clientService;
        this.clientRepository = clientRepository;
        this.clientLoanRepository = clientLoanRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public ResponseEntity<Object> performLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {

        // Validaciones
        ResponseEntity<Object> validationResponse = validateLoan(loanApplicationDTO);
        if (validationResponse != null) {
            return validationResponse;
        }

        Optional<Loan> loanOptional = loanRepository.findById(loanApplicationDTO.getLoanId());
        if (!loanOptional.isPresent()) {
            return new ResponseEntity<>("Loan not found", HttpStatus.NOT_FOUND);
        }

        Loan loan = loanOptional.get();
        ClientDTO authenticatedClientDTO = clientService.getClientAuth(authentication);
        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());

        double loanAmount = loanApplicationDTO.getAmount();
        double totalDebt = loanAmount * 1.20; // Deuda total incluyendo intereses

        ClientLoan clientLoan = new ClientLoan(totalDebt, Collections.singletonList(loanApplicationDTO.getPayments()));

        // Obtener el cliente autenticado
        Client authenticatedClient = clientRepository.findById(authenticatedClientDTO.getId()).orElse(null);

        if (authenticatedClient != null) {
            clientLoan.setClient(authenticatedClient);
        } else {
            return new ResponseEntity<>("Invalid client", HttpStatus.BAD_REQUEST);
        }

        clientLoan.setLoan(loan);

        clientLoanRepository.save(clientLoan);

        String transactionDescription = loan.getName() + " loan approved";
        Transaction transaction = new Transaction(TransactionType.CREDIT, loanAmount, transactionDescription, LocalDateTime.now(), account);
        transactionRepository.save(transaction);

        // Actualizar el saldo de la cuenta
        account.setBalance(account.getBalance() + loanAmount);
        accountRepository.save(account);

        return new ResponseEntity<>("Loan application successful", HttpStatus.CREATED);
    }



    private ResponseEntity<Object> validateLoan(LoanApplicationDTO loanApplicationDTO) {
        if (loanApplicationDTO.getAmount() <= 0 ||
                loanApplicationDTO.getPayments() == null ||
                loanApplicationDTO.getPayments() <= 0) {
            return new ResponseEntity<>("Invalid loan application data", HttpStatus.BAD_REQUEST);
        }

        Loan loan = loanRepository.findById(loanApplicationDTO.getLoanId()).orElse(null);

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

        Account account = accountRepository.findByNumber(loanApplicationDTO.getToAccountNumber());

        if (account == null) {
            return new ResponseEntity<>("Destination account not found", HttpStatus.NOT_FOUND);
        }

        return null;
    }

    public List<LoanDTO> getLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loans.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    private LoanDTO convertToDto(Loan loan) {
        LoanDTO dto = new LoanDTO(loan);
        return dto;
    }
}