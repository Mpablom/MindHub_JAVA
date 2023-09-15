package com.mindhub.homebanking.utils;

import java.util.Random;

public final class CardsUtils {
    private CardsUtils() {
    }
    public static String generateCardNumber() {
        String cardNumber = "";
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
            int randomNumber = random.nextInt(10);
            cardNumber += randomNumber;

            if ((i + 1) % 4 == 0 && i != 15) {
                cardNumber += " ";
            }
        }
        return cardNumber;
    }
    public static int generateCvv() {
        Random random = new Random();
        int cvv = random.nextInt(1000);
        if (cvv < 100) {
            cvv += 100;
        }
        return cvv;
    }
}