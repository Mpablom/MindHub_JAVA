package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.DTO.LoanApplicationDTO;
import com.mindhub.homebanking.DTO.LoanDTO;
import com.mindhub.homebanking.services.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;
    @PostMapping("/loans")
    public ResponseEntity<Object> applyForLoan(@RequestBody LoanApplicationDTO loanApplicationDTO, Authentication authentication) {
        return loanService.performLoan(loanApplicationDTO, authentication);
    }

    @GetMapping("/loans")
    public ResponseEntity<List<LoanDTO>> getAvailableLoans() {
        List<LoanDTO> availableLoans = loanService.getLoans();
        return new ResponseEntity<>(availableLoans, HttpStatus.OK);
    }
}
