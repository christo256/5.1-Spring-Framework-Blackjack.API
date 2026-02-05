package com.blackjack.service;

import com.blackjack.domain.mongo.Game;
import com.blackjack.exception.GameNotFoundException;
import com.blackjack.repository.mongo.GameRepository;
import com.blackjack.service.game.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static reactor.core.publisher.Mono.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void createGame_shouldReturnSavedGame() {
        Game game = new Game();
        game.setPlayerName("Christopher");

        when(gameRepository.save(org.mockito.ArgumentMatchers.any(Game.class)))
                .thenReturn(Mono.just(game));

        Mono<Game> result = gameService.createGame("Christopher");

        StepVerifier.create(result)
                .expectNext(game)
                .verifyComplete();
    }

    @Test
    void getGameById_whenExists_shouldReturnGame() {
        Game game = new Game();
        game.setId("123");

        when(gameRepository.findById("123"))
                .thenReturn(Mono.just(game));

        StepVerifier.create(gameService.getGameById("123"))
                .expectNext(game)
                .verifyComplete();
    }

    @Test
    void getGameById_whenNotExists_shouldThrowException() {
        when(gameRepository.findById("999"))
                .thenReturn(Mono.empty());

        StepVerifier.create(gameService.getGameById("999"))
                .expectError(GameNotFoundException.class)
                .verify();
    }
}

