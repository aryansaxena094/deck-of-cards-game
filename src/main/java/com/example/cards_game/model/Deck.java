package com.example.cards_game.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for(Card.Suit suit : Card.Suit.values()){
            for(Card.Rank rank : Card.Rank.values()){
                cards.add(new Card(suit, rank));
            }
        }
    }
    
    public void shuffle(){
        Collections.shuffle(cards);
    }

    public Card deadCard(){
        if(cards.isEmpty()){
            return null;
        }
        return cards.remove(0);
    }

    public int remainingCardsCount(){
        return cards.size();
    }

    public void addCard(Card card){
        cards.add(card);
    }
}
