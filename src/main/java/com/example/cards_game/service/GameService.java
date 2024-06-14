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

    @Autowired
    private EventLogger eventLogger;

    public String createGame() {
        String gameId = UUID.randomUUID().toString();
        gameStore.createGame(gameId);
        eventLogger.logEvent(gameId, "CREATE_GAME", "Game created with id: " + gameId);
        return gameId;
    }

    public void deleteGame(String gameId) {
        gameStore.deleteGame(gameId);
        eventLogger.logEvent(gameId, "DELETE_GAME", "Game deleted with id: " + gameId);
    }

    public void addPlayerToGame(String gameId, Player player) {
        gameStore.addPlayerToGame(gameId, player);
        eventLogger.logEvent(gameId, "ADD_PLAYER", "Player added to game with id: " + gameId + " player id: " + player.getId());
    }

    public void removePlayerFromGame(String gameId, String playerId) {
        gameStore.removePlayerFromGame(gameId, playerId);
        eventLogger.logEvent(gameId, "REMOVE_PLAYER", "Player removed from game with id: " + gameId + " player id: " + playerId);
    }

    public void addDeckToGame(String gameId, Deck deck) {
        gameStore.addDeckToGame(gameId, deck);
        eventLogger.logEvent(gameId, "ADD_DECK", "Deck added to game with id: " + gameId + " deck id: " + deck.getCards());
    }

    public Card dealCard(String gameId){
        eventLogger.logEvent(gameId, "DEAL_CARD", "Card dealt from game with id: " + gameId);
        return gameStore.dealCard(gameId);
    }

    public Map<String, Game> getAllGames(){
        eventLogger.logEvent("ALL_GAMES", "GET_ALL_GAMES", "All games retrieved");
        return gameStore.getGames();
    }
}
