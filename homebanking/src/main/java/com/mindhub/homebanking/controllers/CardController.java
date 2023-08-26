package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.CardColor;
import com.mindhub.homebanking.models.CardType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class CardController {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    CardRepository cardRepository;
    LocalDate today = LocalDate.now();
    LocalDate expiration = today.plusYears(5);

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType,@RequestParam CardColor cardColor,Authentication authentication){
        Client client = clientRepository.findByEmail(authentication.getName());

        long creditCardCount = client.getCards().stream().filter(card -> card.getType() == CardType.CREDIT).count();

        long debitCardCount = client.getCards().stream().filter(card -> card.getType() == CardType.DEBIT).count();

        if (creditCardCount >= 3 && cardType == CardType.CREDIT) {
            return new ResponseEntity<>("Maximum cards limit reached for CREDIT type", HttpStatus.FORBIDDEN);
        }

        if (debitCardCount >= 3 && cardType == CardType.DEBIT) {
            return new ResponseEntity<>("Maximum cards limit reached for DEBIT type", HttpStatus.FORBIDDEN);
        }


        String cardHolder = client.getFirstName()+" "+client.getLastName();
        String number = generateCardNumber();
        int cvv = generateCvv();
        Card card = new Card(cardHolder,cardType,cardColor,number,cvv,expiration,today,client);
        client.addCards(card);
        cardRepository.save(card);
        clientRepository.save(client);
        return new ResponseEntity<>("Card successfully created", HttpStatus.CREATED);
    }

    //genera los tres números aleatorios de la tarjeta

    public String generateCardNumber() {
        StringBuilder cardNumber = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 16; i++) {
            int randomNumber = random.nextInt(10);
            cardNumber.append(randomNumber);

            if ((i + 1) % 4 == 0 && i != 15) {
                cardNumber.append(" ");
            }
        }

        return cardNumber.toString();
    }

    //genera los tres números aleatorios del cvv

    public int generateCvv() {
        Random random = new Random();
        int cvv = random.nextInt(1000);
        if (cvv < 100) {
            cvv += 100;
        }
        return cvv;
    }

}
