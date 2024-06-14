package com.example.cards_game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Data;

@Data
public class Deck {
    private List<Card> cards;

    public Deck() {
        this.cards = new ArrayList<>();
        initializeStandardDeck();
    }

    private void initializeStandardDeck() {
        for (Card.Suit suit : Card.Suit.values()) {
            for (Card.Rank rank : Card.Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
    }
    
    public Card dealCard() {
        if (cards.isEmpty()) {
            return null;
        }
        return cards.remove(0);
    }

    public int remainingCardsCount() {
        return cards.size();
    }

    @Override
    public String toString() {
        return "Deck{" + cards + '}';
    }
}
