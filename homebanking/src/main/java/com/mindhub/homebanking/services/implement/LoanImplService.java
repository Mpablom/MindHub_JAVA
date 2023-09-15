package com.mindhub.homebanking.services.implement;


import com.mindhub.homebanking.DTO.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.LoanRepository;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;




@Service
public class LoanImplService implements LoanService {

    private final LoanRepository loanRepository;

    @Autowired
    public LoanImplService(LoanRepository loanRepository) {
        this.loanRepository = loanRepository;

    }

    @Override
    public Optional<Loan> findById(Long loanId) {
        return loanRepository.findById(loanId);
    }
    @Override
    public List<LoanDTO> getLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loans.stream().map(this::convertToDto).collect(Collectors.toList());
    }
    private LoanDTO convertToDto(Loan loan) {
        LoanDTO dto = new LoanDTO(loan);
        return dto;
    }
}