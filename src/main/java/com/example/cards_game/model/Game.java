package com.example.cards_game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Game {
    
    @NotNull
    private String id;
    
    @NotNull
    private Map<String, Player> players;
    
    @NotNull
    private List<Deck> gameDeck;
    
    public Game(String id){
        this.id = id;
        this.players = new HashMap<>();
        this.gameDeck = new ArrayList<>();
    }

    public List<Player> getPlayers(){
        ArrayList<Player> playerList = new ArrayList<>(players.values());
        playerList.sort((p1, p2) -> p2.getHandValue() - p1.getHandValue());
        return playerList;
    }

    public void addPlayer(Player player){
        players.put(player.getId(), player);
    }

    public void removePlayer(String playerId){
        players.remove(playerId);
    }

    public Player getPlayer(String playerId){
        return players.get(playerId);
    }

    public void addDeck(Deck deck){
        gameDeck.add(deck);
    }

    public List<Deck> getGameDeck(){
        return new ArrayList<>(gameDeck);
    }

    public Card dealCard(){
        for(Deck deck : gameDeck){
            Card card = deck.dealCard();
            if(card != null){
                return card;
            }
        }
        return null;
    }

    public int remainingCardsCount(){
        int count = 0;
        for(Deck deck : gameDeck){
            count += deck.remainingCardsCount();
        }
        return count;
    }

    public Map<Card.Suit, Integer> getCardsCountBySuit(){
        Map<Card.Suit, Integer> countBySuit = new HashMap<>();
        for(Deck deck : gameDeck){
            for(Card card : deck.getCards()){
                countBySuit.put(card.getSuit(), countBySuit.getOrDefault(card.getSuit(), 0) + 1);
            }
        }
        return countBySuit;
    }
}
