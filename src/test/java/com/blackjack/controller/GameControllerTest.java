package com.blackjack.controller;

import com.blackjack.domain.mongo.Game;
import com.blackjack.domain.mongo.GameStatus;
import com.blackjack.domain.mongo.Hand;
import com.blackjack.service.game.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(GameController.class)
public class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GameService gameService;

    @Test
    void createGame_shouldReturn201() {
        Game game = new Game();
        game.setId("id-123");
        game.setPlayerName("Christopher");
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setPlayerHand(new Hand());
        game.setDealerHand(new Hand());

        Mockito.when(gameService.createGame("Christopher"))
                .thenReturn(Mono.just(game));

        webTestClient.post()
                .uri("/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("{\"playerName\":\"Christopher\"}")
                .exchange()
                .expectStatus().isCreated()
                .expectBody()
                .jsonPath("$.id").isEqualTo("id-123")
                .jsonPath("$.playerName").isEqualTo("Christopher");
    }

    @Test
    void getGameById_shouldReturn200() {
        Game game = new Game();
        game.setId("123");

        game.setPlayerHand(new Hand());
        game.setDealerHand(new Hand());
        game.setStatus(GameStatus.IN_PROGRESS);

        Mockito.when(gameService.getGameById("123"))
                .thenReturn(Mono.just(game));

        webTestClient.get()
                .uri("/game/123")
                .exchange()
                .expectStatus().isOk();
    }
}

