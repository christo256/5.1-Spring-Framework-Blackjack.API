package com.blackjack.blackjack.controller;

import com.blackjack.blackjack.domain.mongo.Game;
import com.blackjack.blackjack.service.game.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private GameService gameService;

    @Test
    void createGame_shouldReturn201() {
        Game game = new Game();
        game.setPlayerName("Christopher");

        Mockito.when(gameService.createGame("Christopher"))
                .thenReturn(Mono.just(game));

        webTestClient.post()
                .uri("/game/new")
                .bodyValue("{\"playerName\":\"Christopher\"}")
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void getGameById_shouldReturn200() {
        Game game = new Game();
        game.setId("123");

        Mockito.when(gameService.getGameById("123"))
                .thenReturn(Mono.just(game));

        webTestClient.get()
                .uri("/game/123")
                .exchange()
                .expectStatus().isOk();
    }
}

