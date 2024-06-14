package com.example.cards_game.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.cards_game.model.Deck;
import com.example.cards_game.model.Game;
import com.example.cards_game.model.Player;

public class ConcurrentGameStoreTest {

    private ConcurrentGameStore concurrentGameStore;

    @BeforeEach
    public void setUp() {
        concurrentGameStore = new ConcurrentGameStore();
    }

    @Test
    public void testCreateGame() {
        String gameId = "game1";
        concurrentGameStore.createGame(gameId);
        Game game = concurrentGameStore.getGame(gameId);
        assertNotNull(game);
        assertEquals(gameId, game.getId());
    }

    @Test
    public void testDeleteGame() {
        String gameId = "game1";
        concurrentGameStore.createGame(gameId);
        concurrentGameStore.deleteGame(gameId);
        Game game = concurrentGameStore.getGame(gameId);
        assertNull(game);
    }

    @Test
    public void testGetGame() {
        String gameId = "game1";
        concurrentGameStore.createGame(gameId);
        Game game = concurrentGameStore.getGame(gameId);
        assertNotNull(game);
        assertEquals(gameId, game.getId());
    }

    @Test
    public void testAddPlayerToGame() {
        String gameId = "game1";
        String playerId = "player1";
        Player player = new Player(playerId);

        concurrentGameStore.createGame(gameId);
        concurrentGameStore.addPlayerToGame(gameId, player);

        Game game = concurrentGameStore.getGame(gameId);
        assertNotNull(game);
        assertEquals(player, game.getPlayer(playerId));
    }

    @Test
    public void testRemovePlayerFromGame() {
        String gameId = "game1";
        String playerId = "player1";
        Player player = new Player(playerId);

        concurrentGameStore.createGame(gameId);
        concurrentGameStore.addPlayerToGame(gameId, player);
        concurrentGameStore.removePlayerFromGame(gameId, playerId);

        Game game = concurrentGameStore.getGame(gameId);
        assertNotNull(game);
        assertNull(game.getPlayer(playerId));
    }

    @Test
    public void testAddDeckToGame() {
        String gameId = "game1";
        Deck deck = new Deck();

        concurrentGameStore.createGame(gameId);
        concurrentGameStore.addDeckToGame(gameId, deck);

        Game game = concurrentGameStore.getGame(gameId);
        assertNotNull(game);
        assertFalse(game.getGameDeck().isEmpty());
    }

    @Test
    public void testGetGames() {
        String gameId1 = "game1";
        String gameId2 = "game2";

        concurrentGameStore.createGame(gameId1);
        concurrentGameStore.createGame(gameId2);

        Map<String, Game> games = concurrentGameStore.getGames();
        assertEquals(2, games.size());
        assertTrue(games.containsKey(gameId1));
        assertTrue(games.containsKey(gameId2));
    }
}