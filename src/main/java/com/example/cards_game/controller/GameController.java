package com.example.cards_game.controller;

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
import org.springframework.web.bind.annotation.RestController;

import com.example.cards_game.model.Card;
import com.example.cards_game.model.Deck;
import com.example.cards_game.model.Game;
import com.example.cards_game.model.Player;
import com.example.cards_game.service.GameService;

@RestController
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    @PostMapping
    public ResponseEntity<String> createGame() {
        String gameId = gameService.createGame();
        return new ResponseEntity<>(gameId, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable String id){
        gameService.deleteGame(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{gameId}/player")
    public ResponseEntity<Void> addPlayerToGame(@PathVariable String gameId, @RequestBody Player player){
        gameService.addPlayerToGame(gameId, player);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{gameId}/player/{playerId}")
    public ResponseEntity<Void> removePlayerFromGame(@PathVariable String gameId, @PathVariable String playerId){
        gameService.removePlayerFromGame(gameId, playerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{gameId}/decks")
    public ResponseEntity<Void> addDeckToGame(@PathVariable String gameId, @RequestBody Deck deck){
        gameService.addDeckToGame(gameId, deck);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{gameId}/deal")
    public ResponseEntity<Void> dealCards(@PathVariable String gameId){
        Card card = gameService.dealCard(gameId);
        if(card != null){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @GetMapping
    public ResponseEntity<Map<String, Game>> getAllGames(){
        Map<String, Game> games = gameService.getAllGames();
        return new ResponseEntity<>(games, HttpStatus.OK);
    }
}
