package com.blackjack.blackjack.service;

import com.blackjack.blackjack.domain.mongo.*;
import com.blackjack.blackjack.repository.mongo.GameRepository;
import com.blackjack.blackjack.exception.GameNotFoundException;
import com.blackjack.blackjack.exception.InvalidGameStateException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;
import java.util.Random;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final Random random = new Random();

    public GameService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Mono<Game> createGame(String playerName) {

        Hand playerHand = new Hand();
        Hand dealerHand = new Hand();

        playerHand.addCard(drawRandomCard());
        playerHand.addCard(drawRandomCard());

        dealerHand.addCard(drawRandomCard());
        dealerHand.addCard(drawRandomCard());

        Game game = new Game();
        game.setPlayerName(playerName);
        game.setPlayerHand(playerHand);
        game.setDealerHand(dealerHand);
        game.setStatus(GameStatus.IN_PROGRESS);
        game.setCreatedAt(Instant.now());

        return gameRepository.save(game);
    }

    private Card drawRandomCard() {
        List<String> suits = List.of("HEARTS", "DIAMONDS", "CLUBS", "SPADES");
        List<String> ranks = List.of("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A");

        String suit = suits.get(random.nextInt(suits.size()));
        String rank = ranks.get(random.nextInt(ranks.size()));

        int value;
        if ("JQK".contains(rank)) {
            value = 10;
        } else if ("A".equals(rank)) {
            value = 11;
        } else {
            value = Integer.parseInt(rank);
        }

        return new Card(suit, rank, value);
    }

    public Mono<Game> getGameById(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)));
    }

    public Mono<Void> deleteGameById(String gameId) {
        return gameRepository.deleteById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)))
                .flatMap(game -> gameRepository.deleteById(gameId));

    }

    public Mono<Game> play(String gameId, MoveType move) {

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)))
                .flatMap(game -> {

                    if (game.getStatus() != GameStatus.IN_PROGRESS) {
                        return Mono.error(new InvalidGameStateException("Game already finished"));
                    }

                    if (move == MoveType.HIT) {
                        game.getPlayerHand().addCard(drawRandomCard());

                        if (game.getPlayerHand().isBust()) {
                            game.setStatus(GameStatus.PLAYER_BUST);
                        }

                    } else if (move == MoveType.STAND) {
                        playDealer(game);
                        resolveGame(game);
                    }

                    return gameRepository.save(game);
                });
    }

    private void playDealer(Game game) {
        while (game.getDealerHand().getScore() < 17) {
            game.getDealerHand().addCard(drawRandomCard());
        }
    }

    private void resolveGame(Game game) {

        if (game.getDealerHand().isBust()) {
            game.setStatus(GameStatus.DEALER_BUST);
            return;
        }

        int playerScore = game.getPlayerHand().getScore();
        int dealerScore = game.getDealerHand().getScore();

        if (playerScore > dealerScore) {
            game.setStatus(GameStatus.PLAYER_WIN);
        } else if (dealerScore > playerScore) {
            game.setStatus(GameStatus.DEALER_WIN);
        } else {
            game.setStatus(GameStatus.PUSH);
        }

    }
}

