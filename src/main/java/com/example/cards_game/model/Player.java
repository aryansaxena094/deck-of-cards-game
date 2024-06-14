package com.example.cards_game.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String id;
    private List<Card> hand;

    public Player() {
        hand = new ArrayList<>();
    }
    
    public Player(String id) {
        this.id = id;
        hand = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCard(Card card){
        hand.add(card);
    }

    public int getHandValue() {
        int value = 0;
        for (Card card : hand) {
            value += card.getRank().ordinal() + 1;
        }
        return value;
    }

    public String toString() {
        return "Player{" + id + ", hand=" + hand + '}';
    }
}
