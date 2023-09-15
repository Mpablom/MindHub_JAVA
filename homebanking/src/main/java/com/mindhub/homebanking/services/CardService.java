package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Card;

import java.util.Optional;

public interface CardService {
    boolean existsByNumber(String cardNumber);
    Card save(Card card);
    void deleteCardById(Long cardId);
    boolean existsById(Long cardId);
    Optional<Card> findById(Long cardId);
}