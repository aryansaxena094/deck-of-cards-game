package com.example.cards_game.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

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
        if (!gameExists(gameId)) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        gameStore.deleteGame(gameId);
        eventLogger.logEvent(gameId, "DELETE_GAME", "Game deleted with id: " + gameId);
    }

    public void addPlayerToGame(String gameId, Player player) {
        if (!gameExists(gameId)) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        gameStore.addPlayerToGame(gameId, player);
        eventLogger.logEvent(gameId, "ADD_PLAYER",
                "Player added to game with id: " + gameId + " player id: " + player.getId());
    }

    public void removePlayerFromGame(String gameId, String playerId) {
        if (!gameExists(gameId) || !playerExists(gameId, playerId)) {
            throw new IllegalArgumentException("Game or player not found: " + gameId + " / " + playerId);
        }
        gameStore.removePlayerFromGame(gameId, playerId);
        eventLogger.logEvent(gameId, "REMOVE_PLAYER",
                "Player removed from game with id: " + gameId + " player id: " + playerId);
    }

    public String addDeckToGame(String gameId) {
        if (!gameExists(gameId)) {
            throw new IllegalArgumentException("Game not found: " + gameId);
        }
        Deck deck = new Deck();
        gameStore.addDeckToGame(gameId, deck);
        eventLogger.logEvent(gameId, "ADD_DECK", "Deck added to game with id: " + gameId);

        Game game = gameStore.getGame(gameId);
        int numberOfDecks = game.getGameDeck().size();
        int totalNumberOfCards = game.getGameDeck().stream().mapToInt(Deck::remainingCardsCount).sum();

        return String.format("{\"GameId\": \"%s\", \"NumberOfDecks\": %d, \"TotalNumberOfCards\": %d}", gameId,
                numberOfDecks, totalNumberOfCards);
    }

    public Game getGame(String gameId) {
        eventLogger.logEvent(gameId, "GET_GAME", "Game retrieved with id: " + gameId);
        return gameStore.getGame(gameId);
    }

    public Map<String, Game> getAllGames() {
        eventLogger.logEvent("ALL_GAMES", "GET_ALL_GAMES", "All games retrieved");
        return gameStore.getGames();
    }

    public String dealCardsEvenly(String gameId) {
        if (!gameExists(gameId)) {
            eventLogger.logEvent(gameId, "DEAL_CARDS", "Game does not exist");
            return "Game not found: " + gameId;
        }

        Game game = gameStore.getGame(gameId);
        List<Player> players = game.getPlayers();

        if (players.isEmpty()) {
            eventLogger.logEvent(gameId, "DEAL_CARDS", "No cards dealt as there are no players in the game");
            return "No players in the game, no cards dealt";
        }

        List<Card> gameDeck = combineAndShuffleDecks(game);
        int numberOfPlayers = players.size();
        int numberOfCards = gameDeck.size();
        int cardsPerPlayer = numberOfCards / numberOfPlayers;
        int remainingCards = numberOfCards % numberOfPlayers;

        for (int i = 0; i < cardsPerPlayer; i++) {
            for (Player player : players) {
                player.addCard(gameDeck.remove(0));
            }
        }
        
        eventLogger.logEvent(gameId, "DEAL_CARDS", "Cards dealt evenly among players. Remaining cards: " + remainingCards);
        return "Cards dealt evenly among players. Remaining cards: " + remainingCards;
    }

    public String dealCardsToPlayer(String gameId, String playerId, int numberOfCards) {
        if (!playerExists(gameId, playerId)) {
            eventLogger.logEvent(gameId, "DEAL_CARDS", "Player with id: " + playerId + " does not exist in the game with id: " + gameId);
            return "Player not found: " + playerId + " in game: " + gameId;
        }

        Game game = gameStore.getGame(gameId);
        Player player = game.getPlayer(playerId);
        List<Card> gameDeck = combineAndShuffleDecks(game);

        for (int i = 0; i < numberOfCards; i++) {
            if (!gameDeck.isEmpty()) {
                player.addCard(gameDeck.remove(0));
            } else {
                break;
            }
        }

        eventLogger.logEvent(gameId, "DEAL_CARD", "Cards dealt to player with ID: " + playerId + " in game with ID: " + gameId + " number of cards: " + numberOfCards);
        return "Cards dealt to player with ID: " + playerId + " in game with ID: " + gameId + " number of cards: " + numberOfCards;
    }

    private List<Card> combineAndShuffleDecks(Game game) {
        List<Card> gameDeck = new ArrayList<>();
        for (Deck deck : game.getGameDeck()) {
            gameDeck.addAll(deck.getCards());
        }
        Collections.shuffle(gameDeck);
        eventLogger.logEvent(game.getId(), "SHUFFLE_DECK", "Deck shuffled");
        return gameDeck;
    }

    public List<Card> getCardsOfPlayer(String gameId, String playerId) {
        if (!playerExists(gameId, playerId)) {
            eventLogger.logEvent(gameId, "GET_CARDS",
                    "Player with id: " + playerId + " does not exist in the game with id: " + gameId);
            return null;
        }
        Game game = gameStore.getGame(gameId);
        Player player = game.getPlayer(playerId);
        eventLogger.logEvent(gameId, "GET_CARDS",
                "Cards retrieved for player with id: " + playerId + " in game with id: " + gameId);
        return player.getHand();
    }

    public Map<Card.Suit, Integer> countsPerSuit(String gameId) {
        Game game = gameStore.getGame(gameId);
        List<Card> gameDeck = combineAndShuffleDecks(game);
        Map<Card.Suit, Integer> suitCount = gameDeck.stream().collect(
                Collectors.groupingBy(Card::getSuit, Collectors.summingInt(suit -> 1)));
        eventLogger.logEvent(gameId, "SUIT_COUNT", "Suit count retrieved");
        return suitCount;
    }

    public boolean gameExists(String gameId) {
        eventLogger.logEvent(gameId, "GAME_EXISTS", "Checking if game with id: " + gameId + " exists");
        return gameStore.getGame(gameId) != null;
    }

    public boolean playerExists(String gameId, String playerId) {
        Game game = gameStore.getGame(gameId);
        eventLogger.logEvent(gameId, "PLAYER_EXISTS",
                "Checking if player with id: " + playerId + " exists in game with id: " + gameId);
        return game != null && game.getPlayer(playerId) != null;
    }
}
