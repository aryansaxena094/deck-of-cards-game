package com.example.cards_game.service;

import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.cards_game.model.Card;
import com.example.cards_game.model.Deck;
import com.example.cards_game.model.Game;
import com.example.cards_game.model.Player;
import com.example.cards_game.store.ConcurrentGameStore;

@Service
public class GameService {
    
    @Autowired
    private ConcurrentGameStore gameStore;

    public String createGame() {
        String gameId = UUID.randomUUID().toString();
        gameStore.createGame(gameId);
        return gameId;
    }

    public void deleteGame(String gameId) {
        gameStore.deleteGame(gameId);
    }

    public void addPlayerToGame(String gameId, Player player) {
        gameStore.addPlayerToGame(gameId, player);
    }

    public void removePlayerFromGame(String gameId, Player player) {
        gameStore.removePlayerFromGame(gameId, player);
    }

    public void addDeckToGame(String gameId, Deck deck) {
        gameStore.addDeckToGame(gameId, deck);
    }

    public Card dealCard(String gameId){
        return gameStore.dealCard(gameId);
    }

    public Map<String, Game> getAllGames(){
        return gameStore.getGames();
    }
}
