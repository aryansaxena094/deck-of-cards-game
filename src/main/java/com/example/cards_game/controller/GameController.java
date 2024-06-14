package com.example.cards_game.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping("/game")
public class GameController {

    private static final Logger logger = LoggerFactory.getLogger(GameController.class);

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<String> createGame() {
        String gameId = gameService.createGame();
        logger.info("Game created with id: {}", gameId);
        return new ResponseEntity<>(gameId, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Game>> getAllGames() {
        Map<String, Game> games = gameService.getAllGames();
        logger.info("All games retrieved");
        return new ResponseEntity<>(games, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id) {
        gameService.deleteGame(id);
        logger.info("Game deleted with id: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{gameId}/deck")
    public ResponseEntity<String> addDeckToGame(@PathVariable String gameId) {
        String responseMessage = gameService.addDeckToGame(gameId);
        logger.info("Deck added to game with ID: {}", gameId);
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }

    @PostMapping("/{gameId}/addplayer")
    public ResponseEntity<Void> addPlayerToGame(@PathVariable String gameId, @RequestBody Player player) {
        gameService.addPlayerToGame(gameId, player);
        logger.info("Player added to game with id: {} player id: {}", gameId, player.getId());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{gameId}/player/{playerId}")
    public ResponseEntity<Void> removePlayerFromGame(@PathVariable String gameId, @PathVariable String playerId) {
        gameService.removePlayerFromGame(gameId, playerId);
        logger.info("Player removed from game with id: {} player id: {}", gameId, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{gameId}/deal")
    public ResponseEntity<String> dealCardsEvenly(@PathVariable String gameId) {
        String responseMessage = gameService.dealCardsEvenly(gameId);
        if (responseMessage.contains("No players")) {
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @PostMapping("/{gameId}/deal/{playerId}")
    public ResponseEntity<String> dealCardsToPlayer(@PathVariable String gameId, @PathVariable String playerId, @RequestParam int numberOfCards) {
        String responseMessage = gameService.dealCardsToPlayer(gameId, playerId, numberOfCards);
        if (responseMessage.contains("Player not found") || responseMessage.contains("Game not found")) {
            return new ResponseEntity<>(responseMessage, HttpStatus.BAD_REQUEST);   
        }
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    @GetMapping("/{gameId}/player/{playerId}/cards")
    public ResponseEntity<List<Card>> getCardsOfPlayer(@PathVariable String gameId, @PathVariable String playerId) {
        List<Card> cards = gameService.getCardsOfPlayer(gameId, playerId);
        if (cards == null) {
            return new ResponseEntity<>(cards, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(cards, HttpStatus.OK);
    }

    @GetMapping("/{gameId}/players")
    public ResponseEntity<List<Player>> getPlayers(@PathVariable String gameId) {
        List<Player> players = gameService.getGame(gameId).getPlayers();
        return new ResponseEntity<>(players, HttpStatus.OK);
    }

    @GetMapping("/{gameId}/suitCount")
    public ResponseEntity<Map<Card.Suit, Integer>> getGameDeck(@PathVariable String gameId) {
        return new ResponseEntity<>(gameService.countsPerSuit(gameId), HttpStatus.OK);
    }
}
