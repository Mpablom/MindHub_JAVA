package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientLoanImplService implements ClientLoanService {
    private final ClientLoanRepository clientLoanRepository;

    @Autowired
    public ClientLoanImplService(ClientLoanRepository clientLoanRepository) {
        this.clientLoanRepository = clientLoanRepository;
    }

    @Override
     public ClientLoan save(ClientLoan clientLoan){
        return clientLoanRepository.save(clientLoan) ;
    }
}
