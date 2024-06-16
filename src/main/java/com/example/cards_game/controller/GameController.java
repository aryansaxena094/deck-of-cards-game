package com.example.cards_game.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.cards_game.model.Card;
import com.example.cards_game.model.Game;
import com.example.cards_game.model.Player;
import com.example.cards_game.service.GameService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/game")
@Slf4j
@Tag(name = "Game Controller", description = "Operations pertaining to the game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    @Operation(summary = "Create a new game", description = "Create a new game and return the game ID")
    public ResponseEntity<String> createGame() {
        String gameId = gameService.createGame();
        log.info("Game created with id: {}", gameId);
        return new ResponseEntity<>(gameId, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all games", description = "Get all games and return a map of game IDs to games")
    public ResponseEntity<Map<String, Game>> getAllGames() {
        Map<String, Game> games = gameService.getAllGames();
        log.info("All games retrieved");
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a game", description = "Delete a game by ID")
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        gameService.deleteGame(id);
        log.info("Game deleted with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{gameId}/deck")
    @Operation(summary = "Add a deck to a game", description = "Add a deck to a game by ID")
    public ResponseEntity<String> addDeckToGame(@PathVariable String gameId) {
        String responseMessage = gameService.addDeckToGame(gameId);
        log.info("Deck added to game with ID: {}", gameId);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @PostMapping("/{gameId}/addplayer")
    @Operation(summary = "Add a player to a game", description = "Add a player to a game by ID")
    public ResponseEntity<Void> addPlayerToGame(@PathVariable String gameId, @RequestBody Player player) {
        gameService.addPlayerToGame(gameId, player);
        log.info("Player added to game with id: {} player id: {}", gameId, player.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{gameId}/player/{playerId}")
    @Operation(summary = "Remove a player from a game", description = "Remove a player from a game by ID")
    public ResponseEntity<Void> removePlayerFromGame(@PathVariable String gameId, @PathVariable String playerId) {
        gameService.removePlayerFromGame(gameId, playerId);
        log.info("Player removed from game with id: {} player id: {}", gameId, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{gameId}/deal")
    @Operation(summary = "Deal cards evenly to all players", description = "Deal cards evenly to all players in a game by ID")
    public ResponseEntity<String> dealCardsEvenly(@PathVariable String gameId) {
        String responseMessage = gameService.dealCardsEvenly(gameId);
        if (responseMessage.contains("No players")) {
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/{gameId}/deal/{playerId}")
    @Operation(summary = "Deal cards to a player", description = "Deal cards to a player in a game by ID")  
    public ResponseEntity<String> dealCardsToPlayer(@PathVariable String gameId, @PathVariable String playerId, @RequestParam int numberOfCards) {
        String responseMessage = gameService.dealCardsToPlayer(gameId, playerId, numberOfCards);
        if (responseMessage.contains("Player not found") || responseMessage.contains("Game not found")) {
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);   
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/{gameId}/player/{playerId}/cards")
    @Operation(summary = "Get cards of a player", description = "Get cards of a player in a game by ID")
    public ResponseEntity<List<Card>> getCardsOfPlayer(@PathVariable String gameId, @PathVariable String playerId) {
        List<Card> cards = gameService.getCardsOfPlayer(gameId, playerId);
        if (cards == null) {
            return new ResponseEntity<>(cards, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping("/{gameId}/players")
    @Operation(summary = "Get players of a game", description = "Get players of a game by ID")
    public ResponseEntity<List<Player>> getPlayers(@PathVariable String gameId) {
        List<Player> players = gameService.getGame(gameId).getPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/{gameId}/suitCount")
    @Operation(summary = "Get the count of each suit in the game deck", description = "Get the count of each suit in the game deck by ID")  
    public ResponseEntity<Map<Card.Suit, Integer>> getGameDeck(@PathVariable String gameId) {
        return new ResponseEntity<>(gameService.countsPerSuit(gameId), HttpStatus.OK);
    }
}
