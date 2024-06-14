package com.example.cards_game.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.example.cards_game.model.Card;
import com.example.cards_game.model.Deck;
import com.example.cards_game.model.Game;
import com.example.cards_game.model.Player;
import com.example.cards_game.store.ConcurrentGameStore;

public class GameServiceTest {

    @Mock
    private ConcurrentGameStore gameStore;

    @Mock
    private EventLogger eventLogger;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateGame() {
        String gameId = gameService.createGame();
        assertNotNull(gameId);
        verify(gameStore, times(1)).createGame(gameId);
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("CREATE_GAME"), anyString());
    }

    @Test
    public void testDeleteGame() {
        String gameId = UUID.randomUUID().toString();
        when(gameStore.getGame(gameId)).thenReturn(new Game(gameId));
        gameService.deleteGame(gameId);
        verify(gameStore, times(1)).deleteGame(eq(gameId));
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("DELETE_GAME"), anyString());
    }

    @Test
    public void testAddPlayerToGame() {
        String gameId = UUID.randomUUID().toString();
        Player player = new Player("player1");
        when(gameStore.getGame(gameId)).thenReturn(new Game(gameId));

        gameService.addPlayerToGame(gameId, player);

        verify(gameStore, times(1)).addPlayerToGame(eq(gameId), eq(player));
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("ADD_PLAYER"), anyString());
    }

    @Test
    public void testRemovePlayerFromGame() {
        String gameId = UUID.randomUUID().toString();
        String playerId = "player1";
        Game game = new Game(gameId);
        game.addPlayer(new Player(playerId));
        when(gameStore.getGame(gameId)).thenReturn(game);

        gameService.removePlayerFromGame(gameId, playerId);

        verify(gameStore, times(1)).removePlayerFromGame(eq(gameId), eq(playerId));
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("REMOVE_PLAYER"), anyString());
    }

    @Test
    public void testAddDeckToGame() {
        String gameId = UUID.randomUUID().toString();
        Game game = new Game(gameId);
        when(gameStore.getGame(gameId)).thenReturn(game);

        String response = gameService.addDeckToGame(gameId);

        assertNotNull(response);
        verify(gameStore, times(1)).addDeckToGame(eq(gameId), any(Deck.class));
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("ADD_DECK"), anyString());
    }

    @Test
    public void testDealCardsEvenly() {
        String gameId = UUID.randomUUID().toString();
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        Game game = new Game(gameId);
        game.addPlayer(player1);
        game.addPlayer(player2);
        Deck deck = new Deck();
        game.addDeck(deck);
        when(gameStore.getGame(gameId)).thenReturn(game);

        String response = gameService.dealCardsEvenly(gameId);

        assertNotNull(response);
        assertEquals(26, player1.getHand().size());
        assertEquals(26, player2.getHand().size());
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("DEAL_CARDS"), anyString());
    }

    @Test
    public void testDealCardsToPlayer() {
        String gameId = UUID.randomUUID().toString();
        String playerId = "player1";
        Player player = new Player(playerId);
        Game game = new Game(gameId);
        game.addPlayer(player);
        Deck deck = new Deck();
        game.addDeck(deck);
        when(gameStore.getGame(gameId)).thenReturn(game);

        String response = gameService.dealCardsToPlayer(gameId, playerId, 5);

        assertNotNull(response);
        assertEquals(5, player.getHand().size());
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("DEAL_CARD"), anyString());
    }

    @Test
    public void testGetCardsOfPlayer() {
        String gameId = UUID.randomUUID().toString();
        String playerId = "player1";
        Player player = new Player(playerId);
        Game game = new Game(gameId);
        game.addPlayer(player);
        when(gameStore.getGame(gameId)).thenReturn(game);

        List<Card> cards = gameService.getCardsOfPlayer(gameId, playerId);

        assertNotNull(cards);
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("GET_CARDS"), anyString());
    }

    @Test
    public void testCountsPerSuit() {
        String gameId = UUID.randomUUID().toString();
        Game game = new Game(gameId);
        Deck deck = new Deck();
        game.addDeck(deck);
        when(gameStore.getGame(gameId)).thenReturn(game);

        Map<Card.Suit, Integer> suitCounts = gameService.countsPerSuit(gameId);

        assertNotNull(suitCounts);
        assertEquals(4, suitCounts.size());
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("SUIT_COUNT"), anyString());
    }

    @Test
    public void testGameExists() {
        String gameId = UUID.randomUUID().toString();
        when(gameStore.getGame(gameId)).thenReturn(new Game(gameId));

        boolean exists = gameService.gameExists(gameId);

        assertTrue(exists);
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("GAME_EXISTS"), anyString());
    }

    @Test
    public void testPlayerExists() {
        String gameId = UUID.randomUUID().toString();
        String playerId = "player1";
        Player player = new Player(playerId);
        Game game = new Game(gameId);
        game.addPlayer(player);
        when(gameStore.getGame(gameId)).thenReturn(game);

        boolean exists = gameService.playerExists(gameId, playerId);

        assertTrue(exists);
        verify(eventLogger, times(1)).logEvent(eq(gameId), eq("PLAYER_EXISTS"), anyString());
    }
}
