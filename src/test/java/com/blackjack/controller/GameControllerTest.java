package com.blackjack.controller;

import com.blackjack.domain.mongo.Game;
import com.blackjack.domain.mongo.GameStatus;
import com.blackjack.domain.mongo.Hand;
import com.blackjack.domain.mongo.MoveType;
import com.blackjack.service.game.GameService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@WebFluxTest(controllers = GameController.class)
@Import(com.blackjack.mapper.GameMapper.class)
class GameControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private GameService gameService;

    private Game buildValidGame() {
        Game game = new Game();
        game.setId("1");
        game.setPlayerName("Chris");
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setPlayerHand(new Hand());
        game.setDealerHand(new Hand());
        return game;
    }

    @Test
    void createGame_whenPlayerNameIsValid_shouldReturnCreatedGame() {

        Game game = buildValidGame();
        Mockito.when(gameService.createGame("Chris")).thenReturn(Mono.just(game));

        webTestClient.post()
                .uri("/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "playerName": "Chris"
                        }
                        """)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.playerName").isEqualTo("Chris")
                .jsonPath("$.status").isEqualTo("IN_PROGRESS");

        Mockito.verify(gameService).createGame("Chris");
    }

    @Test
    void getGameById_whenGameExists_shouldReturnGame() {

        Game game = buildValidGame();
        Mockito.when(gameService.getGameById("1")).thenReturn(Mono.just(game));

        webTestClient.get()
                .uri("/game/1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.playerName").isEqualTo("Chris")
                .jsonPath("$.status").isEqualTo("IN_PROGRESS");

        Mockito.verify(gameService).getGameById("1");
    }

    @Test
    void play_whenMoveIsHit_shouldReturnUpdatedGame() {

        Game game = buildValidGame();
        Mockito.when(gameService.play("1", MoveType.HIT)).thenReturn(Mono.just(game));

        webTestClient.post()
                .uri("/game/1/play")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "move": "HIT"
                        }
                        """)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo("1")
                .jsonPath("$.status").isEqualTo("IN_PROGRESS");

        Mockito.verify(gameService).play("1", MoveType.HIT);
    }

    @Test
    void deleteGame_whenGameExists_shouldReturnNoContent() {

        Mockito.when(gameService.deleteGameById("1")).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/game/1/delete")
                .exchange()
                .expectStatus().isNoContent();

        Mockito.verify(gameService).deleteGameById("1");
    }

    @Test
    void createGame_whenNameInvalid_shouldReturnBadRequest() {

        webTestClient.post()
                .uri("/game/new")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "playerName": ""
                        }
                        """)
                .exchange()
                .expectStatus().isBadRequest();
    }

}
