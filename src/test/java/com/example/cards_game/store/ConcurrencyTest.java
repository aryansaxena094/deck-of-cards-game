package com.example.cards_game.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.example.cards_game.model.Deck;
import com.example.cards_game.model.Game;
import com.example.cards_game.model.Player;

public class ConcurrencyTest {

    private ConcurrentGameStore concurrentGameStore;

    @BeforeEach
    public void setUp() {
        concurrentGameStore = new ConcurrentGameStore();
    }

    @Test
    public void testConcurrentAddPlayerToGame() throws InterruptedException {
        String gameId = "game1";
        concurrentGameStore.createGame(gameId);

        int numberOfThreads = 10;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            final int playerId = i;
            executorService.submit(() -> {
                try {
                    Player player = new Player("player" + playerId);
                    concurrentGameStore.addPlayerToGame(gameId, player);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Game game = concurrentGameStore.getGame(gameId);
        assertNotNull(game);
        assertEquals(numberOfThreads, game.getPlayers().size());
    }

    @Test
    public void testConcurrentAddAndRemovePlayerToGame() throws InterruptedException {
        String gameId = "game1";
        concurrentGameStore.createGame(gameId);

        int numberOfThreads = 10;
        CountDownLatch addLatch = new CountDownLatch(numberOfThreads);
        CountDownLatch removeLatch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads * 2);

        for (int i = 0; i < numberOfThreads; i++) {
            final int playerId = i;
            executorService.submit(() -> {
                try {
                    Player player = new Player("player" + playerId);
                    concurrentGameStore.addPlayerToGame(gameId, player);
                } finally {
                    addLatch.countDown();
                }
            });

            executorService.submit(() -> {
                try {
                    addLatch.await();
                    String playerToRemove = "player" + playerId;
                    concurrentGameStore.removePlayerFromGame(gameId, playerToRemove);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    removeLatch.countDown();
                }
            });
        }

        removeLatch.await();
        executorService.shutdown();

        Game game = concurrentGameStore.getGame(gameId);
        assertNotNull(game);
        assertEquals(0, game.getPlayers().size());
    }

    @Test
    public void testConcurrentAddDeckToGame() throws InterruptedException {
        String gameId = "game1";
        concurrentGameStore.createGame(gameId);

        int numberOfThreads = 10;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {
                    Deck deck = new Deck();
                    concurrentGameStore.addDeckToGame(gameId, deck);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        Game game = concurrentGameStore.getGame(gameId);
        assertNotNull(game);
        assertEquals(numberOfThreads, game.getGameDeck().size());
    }
}