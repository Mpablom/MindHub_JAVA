package com.mindhub.homebanking.services;

import com.mindhub.homebanking.DTO.LoanApplicationDTO;
import com.mindhub.homebanking.DTO.LoanDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface LoanService {
    ResponseEntity<Object> performLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication);
    public List<LoanDTO> getLoans();
}
