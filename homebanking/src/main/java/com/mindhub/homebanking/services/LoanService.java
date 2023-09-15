package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.LoanDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Loan;

import java.util.List;
import java.util.Optional;

public interface LoanService {
    List<LoanDTO> getLoans();
    Optional<Loan> findById(Long loanId);
}
