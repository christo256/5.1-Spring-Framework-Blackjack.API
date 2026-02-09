package com.blackjack.controller;

import com.blackjack.dto.PlayerRankingResponse;
import com.blackjack.service.player.PlayerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.http.MediaType;

import java.util.List;

@WebFluxTest(controllers = PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private PlayerService playerService;

    @Test
    void updatePlayerName_shouldReturnOk() {

        webTestClient.put()
                .uri("/player/1")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue("""
                        {
                          "name": "NewName"
                        }
                        """)
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(playerService).updatePlayerName(1L, "NewName");
    }

    @Test
    void getRanking_shouldReturnRankingList() {

        List<PlayerRankingResponse> ranking = List.of(
                new PlayerRankingResponse("Chris", 10, 7),
                new PlayerRankingResponse("Alex", 5, 2)
        );

        Mockito.when(playerService.getRanking()).thenReturn(ranking);

        webTestClient.get()
                .uri("/player/ranking")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$[0].playerName").isEqualTo("Chris")
                .jsonPath("$[0].gamesPlayed").isEqualTo(10)
                .jsonPath("$[0].gamesWon").isEqualTo(7);

        Mockito.verify(playerService).getRanking();
    }
}
