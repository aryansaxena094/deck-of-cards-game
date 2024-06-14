package com.example.cards_game.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.example.cards_game.model.Card;
import com.example.cards_game.model.Game;
import com.example.cards_game.model.Player;
import com.example.cards_game.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@WebMvcTest(GameController.class)
public class GameControllerTest {
    private MockMvc mockMvc;

    @MockBean
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(gameController).build();
    }

    @Test
    public void testCreateGame() throws Exception {
        when(gameService.createGame()).thenReturn("game1");

        mockMvc.perform(post("/game"))
                .andExpect(status().isCreated())
                .andExpect(content().string("game1"));

        verify(gameService, times(1)).createGame();
    }

    @Test
    public void testGetAllGames() throws Exception {
        Map<String, Game> games = new HashMap<>();
        games.put("game1", new Game("game1"));
        when(gameService.getAllGames()).thenReturn(games);

        mockMvc.perform(get("/game"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game1").exists());

        verify(gameService, times(1)).getAllGames();
    }

    @Test
    public void testDeleteGame() throws Exception {
        String gameId = "game1";

        mockMvc.perform(delete("/game/{id}", gameId))
                .andExpect(status().isNoContent());

        verify(gameService, times(1)).deleteGame(gameId);
    }

    @Test
    public void testAddDeckToGame() throws Exception {
        String gameId = "game1";
        String responseMessage = "{\"GameId\": \"game1\", \"NumberOfDecks\": 1, \"TotalNumberOfCards\": 52}";
        when(gameService.addDeckToGame(gameId)).thenReturn(responseMessage);

        mockMvc.perform(post("/game/{gameId}/deck", gameId))
                .andExpect(status().isCreated())
                .andExpect(content().string(responseMessage));

        verify(gameService, times(1)).addDeckToGame(gameId);
    }

    @Test
    public void testAddPlayerToGame() throws Exception {
        String gameId = "game1";
        Player player = new Player("player1");

        mockMvc.perform(post("/game/{gameId}/addplayer", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(player)))
                .andExpect(status().isCreated());

        verify(gameService, times(1)).addPlayerToGame(eq(gameId), any(Player.class));
    }

    @Test
    public void testRemovePlayerFromGame() throws Exception {
        String gameId = "game1";
        String playerId = "player1";

        mockMvc.perform(delete("/game/{gameId}/player/{playerId}", gameId, playerId))
                .andExpect(status().isNoContent());

        verify(gameService, times(1)).removePlayerFromGame(gameId, playerId);
    }

    @Test
    public void testDealCardsEvenly() throws Exception {
        String gameId = "game1";
        String responseMessage = "Cards dealt evenly among players. Remaining cards: 0";
        when(gameService.dealCardsEvenly(gameId)).thenReturn(responseMessage);

        mockMvc.perform(post("/game/{gameId}/deal", gameId))
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));

        verify(gameService, times(1)).dealCardsEvenly(gameId);
    }

    @Test
    public void testDealCardsToPlayer() throws Exception {
        String gameId = "game1";
        String playerId = "player1";
        String responseMessage = "Cards dealt to player with ID: player1 in game with ID: game1 number of cards: 5";
        when(gameService.dealCardsToPlayer(gameId, playerId, 5)).thenReturn(responseMessage);

        mockMvc.perform(post("/game/{gameId}/deal/{playerId}", gameId, playerId)
                .param("numberOfCards", "5"))
                .andExpect(status().isOk())
                .andExpect(content().string(responseMessage));

        verify(gameService, times(1)).dealCardsToPlayer(gameId, playerId, 5);
    }

    @Test
    public void testGetCardsOfPlayer() throws Exception {
        String gameId = "game1";
        String playerId = "player1";
        List<Card> cards = Arrays.asList(new Card(Card.Suit.HEARTS, Card.Rank.ACE));
        when(gameService.getCardsOfPlayer(gameId, playerId)).thenReturn(cards);

        mockMvc.perform(get("/game/{gameId}/player/{playerId}/cards", gameId, playerId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].suit").value("HEARTS"))
                .andExpect(jsonPath("$[0].rank").value("ACE"));

        verify(gameService, times(1)).getCardsOfPlayer(gameId, playerId);
    }

    @Test
    public void testGetPlayers() throws Exception {
        String gameId = "game1";
        List<Player> players = Arrays.asList(new Player("player1"), new Player("player2"));
        Game game = new Game(gameId);
        for (Player player : players) {
            game.addPlayer(player);
        }
        when(gameService.getGame(gameId)).thenReturn(game);

        mockMvc.perform(get("/game/{gameId}/players", gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("player1"))
                .andExpect(jsonPath("$[1].id").value("player2"));

        verify(gameService, times(1)).getGame(gameId);
    }

    @Test
    public void testGetGameDeck() throws Exception {
        String gameId = "game1";
        Map<Card.Suit, Integer> suitCounts = new HashMap<>();
        suitCounts.put(Card.Suit.HEARTS, 13);
        suitCounts.put(Card.Suit.SPADES, 13);
        suitCounts.put(Card.Suit.DIAMONDS, 13);
        suitCounts.put(Card.Suit.CLUBS, 13);
        when(gameService.countsPerSuit(gameId)).thenReturn(suitCounts);

        mockMvc.perform(get("/game/{gameId}/suitCount", gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.HEARTS").value(13))
                .andExpect(jsonPath("$.SPADES").value(13))
                .andExpect(jsonPath("$.DIAMONDS").value(13))
                .andExpect(jsonPath("$.CLUBS").value(13));

        verify(gameService, times(1)).countsPerSuit(gameId);
    }
}
