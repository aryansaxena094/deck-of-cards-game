package com.example.cards_game.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.stereotype.Component;

import com.example.cards_game.model.Deck;
import com.example.cards_game.model.Game;
import com.example.cards_game.model.Player;

@Component
public class ConcurrentGameStore {

    private final Map<String, Game> games = new HashMap<>();
    private final Lock lock = new ReentrantLock();

    public void createGame(String gameId) {
        lock.lock();
        try {
            games.put(gameId, new Game(gameId));
        } finally {
            lock.unlock();
        }
    }

    public void deleteGame(String gameId) {
        lock.lock();
        try {
            games.remove(gameId);
        } finally {
            lock.unlock();
        }
    }

    public Game getGame(String gameId) {
        lock.lock();
        try {
            return games.get(gameId);
        } finally {
            lock.unlock();
        }
    }

    public void addPlayerToGame(String gameId, Player player) {
        lock.lock();
        try {
            Game game = games.get(gameId);
            if (game != null) {
                game.addPlayer(player);
            }
        } finally {
            lock.unlock();
        }
    }

    public void removePlayerFromGame(String gameId, String playerId) {
        lock.lock();
        try {
            Game game = games.get(gameId);
            if (game != null) {
                game.removePlayer(playerId);
            }
        } finally {
            lock.unlock();
        }
    }

    public void addDeckToGame(String gameId, Deck deck) {
        lock.lock();
        try {
            Game game = games.get(gameId);
            if (game != null) {
                game.addDeck(deck);
            }
        } finally {
            lock.unlock();
        }
    }

    public Map<String, Game> getGames() {
        lock.lock();
        try {
            return new HashMap<>(games);
        } finally {
            lock.unlock();
        }
    }
}
