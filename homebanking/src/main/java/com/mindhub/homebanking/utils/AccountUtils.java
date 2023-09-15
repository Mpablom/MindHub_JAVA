package com.mindhub.homebanking.utils;

import java.util.Random;

public final class AccountUtils {
    private AccountUtils() {
    }
    public static String generateAccountNumber() {
        StringBuilder accountNumber;
        accountNumber = new StringBuilder("VIN-");
        Random random = new Random();

        for (int i = 0; i < 8; i++) {
            int randomNumber = random.nextInt(10);
            accountNumber.append(randomNumber);
        }
        return accountNumber.toString();
    }
}
