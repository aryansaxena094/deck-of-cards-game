package com.example.cards_game.model;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Player {

    @NotNull
    private String id;

    private List<Card> hand;

    public Player() {
        hand = new ArrayList<>();
    }
    
    public Player(String id) {
        this.id = id;
        hand = new ArrayList<>();
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

    @Override
    public String toString() {
        return "Player{" + id + ", hand=" + hand + '}';
    }
}
