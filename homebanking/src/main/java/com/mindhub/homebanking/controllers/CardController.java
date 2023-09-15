package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import com.mindhub.homebanking.utils.CardsUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CardController {

    private final ClientService clientService;
    private final CardService cardService;
    private final AccountService accountService;

    @Autowired
    public CardController(ClientService clientService, CardService cardService, AccountService accountService) {
        this.clientService = clientService;
        this.cardService = cardService;
        this.accountService = accountService;
    }

    @PostMapping("/clients/current/cards")
    public ResponseEntity<Object> createCard(@RequestParam CardType cardType,@RequestParam CardColor cardColor,Authentication authentication){
        LocalDate today = LocalDate.now();
        LocalDate expiration = today.plusYears(5);

        if (cardType == null || cardColor == null) {
            return new ResponseEntity<>("Card type and card color cannot be empty", HttpStatus.BAD_REQUEST);
        }

        Client client = clientService.findByEmail(authentication.getName());

        long creditCardCount = client.getCards().stream().filter(card -> card.getType() == CardType.CREDIT).count();

        long debitCardCount = client.getCards().stream().filter(card -> card.getType() == CardType.DEBIT).count();

        if (creditCardCount >= 3 && cardType == CardType.CREDIT) {
            return new ResponseEntity<>("Maximum cards limit reached for CREDIT type", HttpStatus.FORBIDDEN);
        }

        if (debitCardCount >= 3 && cardType == CardType.DEBIT) {
            return new ResponseEntity<>("Maximum cards limit reached for DEBIT type", HttpStatus.FORBIDDEN);
        }


        String cardHolder = client.getFirstName()+" "+client.getLastName();
        String number = CardsUtils.generateCardNumber();
        int cvv = CardsUtils.generateCvv();
        while (cardService.existsByNumber(number)){
            number = CardsUtils.generateCardNumber();
        }
            Card card = new Card(cardHolder,cardType,cardColor,number,cvv,expiration,today, client,true);
            client.addCards(card);
            cardService.save(card);
            clientService.save(client);
        return new ResponseEntity<>("Card successfully created", HttpStatus.CREATED);
    }
    @DeleteMapping("/cards/{id}")
    public ResponseEntity<String> deactivateCard(@PathVariable Long id) {
        try {
            Optional<Card> cardOptional = cardService.findById(id);
            if (cardOptional.isPresent()) {
                Card card = cardOptional.get();

                card.setActive(false); // Desactiva la tarjeta
                cardService.save(card); // Guarda la tarjeta actualizada

                return new ResponseEntity<>("Card deactivated successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Card not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error deactivating card: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
