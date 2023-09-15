package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CardImplService implements CardService {

     private final CardRepository cardRepository;

    @Autowired
    public CardImplService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }
    @Override
    public boolean existsByNumber(String cardNumber){
        return cardRepository.existsByNumber(cardNumber);
    }
    @Override
    public Card save(Card card){
        return cardRepository.save(card);
    }
    @Override
    @Transactional
    public void deleteCardById(Long cardId){
        cardRepository.deleteById(cardId);
    }
    @Override
    public boolean existsById(Long cardId){
        return cardRepository.existsById(cardId);
    }

    @Override
    public Optional<Card> findById(Long cardId){
        return cardRepository.findById(cardId);
    }
}
