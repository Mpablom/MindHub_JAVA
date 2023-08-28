package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class AccountImplService implements AccountService {
    @Autowired
    private AccountService accountService;

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
