package com.blackjack.service;

import com.blackjack.domain.mongo.*;
import com.blackjack.domain.mongo.enums.Rank;
import com.blackjack.domain.mongo.enums.Suit;
import com.blackjack.domain.sql.Player;
import com.blackjack.repository.mongo.GameRepository;
import com.blackjack.service.game.BlackJackRulesService;
import com.blackjack.service.game.DeckService;
import com.blackjack.service.game.GameService;
import com.blackjack.service.player.PlayerService;
import com.blackjack.service.player.PlayerStatsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GameServiceTest {

    private GameRepository gameRepository;
    private DeckService deckService;
    private PlayerService playerService;
    private PlayerStatsService playerStatsService;
    private BlackJackRulesService rulesService;

    private GameService gameService;

    @BeforeEach
    void setup() {

        gameRepository = mock(GameRepository.class);
        deckService = mock(DeckService.class);
        playerService = mock(PlayerService.class);
        playerStatsService = mock(PlayerStatsService.class);
        rulesService = new BlackJackRulesService();

        gameService = new GameService(
                gameRepository,
                deckService,
                playerService,
                rulesService,
                playerStatsService
        );
    }

    @Test
    void createGame_shouldReturnGame() {

        Player player = new Player("Chris");
        player.setId(10L);

        when(playerService.findOrCreate("Chris")).thenReturn(player);

        List<Card> fakeDeck = new ArrayList<>(List.of(
                new Card(Suit.HEARTS, Rank.A),
                new Card(Suit.CLUBS, Rank.TWO),
                new Card(Suit.SPADES, Rank.THREE),
                new Card(Suit.DIAMONDS, Rank.FOUR)
        ));


        when(deckService.createNewDeck()).thenReturn(fakeDeck);
        when(gameRepository.save(any())).thenAnswer(i -> Mono.just(i.getArgument(0)));

        Mono<Game> result = gameService.createGame("Chris");

        StepVerifier.create(result)
                .assertNext(game -> {
                    assert game.getPlayerId().equals("10");
                    assert game.getPlayerHand() != null;
                    assert game.getDealerHand() != null;
                    assert game.getStatus() == GameStatus.IN_PROGRESS;
                })
                .verifyComplete();
    }

    @Test
    void play_hit_shouldAddCard() {

        Game game = new Game();
        game.setId("1");
        game.setPlayerId("10");
        game.setStatus(GameStatus.IN_PROGRESS);

        Hand playerHand = new Hand();
        Hand dealerHand = new Hand();

        game.setPlayerHand(playerHand);
        game.setDealerHand(dealerHand);

        game.setDeck(new ArrayList<>(List.of(
                new Card(Suit.HEARTS, Rank.A)
        )));

        when(gameRepository.findById("1")).thenReturn(Mono.just(game));
        when(gameRepository.save(any())).thenReturn(Mono.just(game));

        Mono<Game> result = gameService.play("1", MoveType.HIT);

        StepVerifier.create(result)
                .assertNext(g -> {
                    assertEquals(1, g.getPlayerHand().getCards().size());
                    assertEquals(GameStatus.IN_PROGRESS, g.getStatus());
                })
                .verifyComplete();
    }

    @Test
    void play_stand_shouldResolveGame() {

        Game game = new Game();
        game.setId("1");
        game.setPlayerId("10");
        game.setStatus(GameStatus.IN_PROGRESS);

        Hand playerHand = new Hand();
        playerHand.setScore(20);

        Hand dealerHand = new Hand();
        dealerHand.setScore(18);

        game.setPlayerHand(playerHand);
        game.setDealerHand(dealerHand);

        game.setDeck(List.of(
                new Card(Suit.HEARTS, Rank.TWO)
        ));

        when(gameRepository.findById("1")).thenReturn(Mono.just(game));
        when(gameRepository.save(any())).thenReturn(Mono.just(game));

        Mono<Game> result = gameService.play("1", MoveType.STAND);

        StepVerifier.create(result)
                .assertNext(g -> {
                    assert g.getStatus() != GameStatus.IN_PROGRESS;
                })
                .verifyComplete();

        verify(playerStatsService).updateStats(eq(10L), any());
    }

    @Test
    void getGameById_shouldReturnGame() {

        Game game = new Game();
        game.setId("1");

        when(gameRepository.findById("1")).thenReturn(Mono.just(game));

        StepVerifier.create(gameService.getGameById("1"))
                .expectNext(game)
                .verifyComplete();
    }

    @Test
    void deleteGame_shouldDelete() {

        when(gameRepository.existsById("1")).thenReturn(Mono.just(true));
        when(gameRepository.deleteById("1")).thenReturn(Mono.empty());

        StepVerifier.create(gameService.deleteGameById("1"))
                .verifyComplete();

        verify(gameRepository).existsById("1");
        verify(gameRepository).deleteById("1");
    }

    @Test
    void deleteGame_whenNotExists_shouldError() {

        when(gameRepository.existsById("1")).thenReturn(Mono.just(false));

        StepVerifier.create(gameService.deleteGameById("1"))
                .expectError()
                .verify();
    }


    @Test
    void play_whenGameNotFound_shouldError() {

        when(gameRepository.findById("1")).thenReturn(Mono.empty());

        StepVerifier.create(gameService.play("1", MoveType.HIT))
                .expectError()
                .verify();
    }

    @Test
    void play_whenGameFinished_shouldError() {

        Game game = new Game();
        game.setId("1");
        game.setStatus(GameStatus.PLAYER_WIN);

        when(gameRepository.findById("1")).thenReturn(Mono.just(game));

        StepVerifier.create(gameService.play("1", MoveType.HIT))
                .expectError()
                .verify();
    }

}

