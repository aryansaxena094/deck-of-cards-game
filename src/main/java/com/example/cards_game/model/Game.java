package com.example.cards_game.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Game {
    private String id;
    private Map<String, Player> players;
    private List<Deck> gameDeck;
    
    public Game(String id){
        this.id = id;
        this.players = new HashMap<>();
        this.gameDeck = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public List<Player> getPlayers(){
        return new ArrayList<>(players.values());
    }

    public void addPlayer(Player player){
        players.put(player.getId(), player);
    }

    public void removePlayer(String playerId){
        players.remove(playerId);
    }

    public void addDeck(Deck deck){
        gameDeck.add(deck);
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
