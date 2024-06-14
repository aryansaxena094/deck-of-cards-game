package com.example.cards_game.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {

    @NotNull
    private Suit suit;
    
    @NotNull
    private Rank rank;

    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    public enum Rank {
        ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING
    }
    
    @Override
    public String toString() {
        return rank + " of " + suit;
    }
}
