package com.blackjack.blackjack.service;

import com.blackjack.blackjack.domain.mongo.*;
import com.blackjack.blackjack.repository.mongo.GameRepository;
import com.blackjack.blackjack.exception.GameNotFoundException;
import com.blackjack.blackjack.exception.InvalidGameStateException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Random;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final DeckService deckService;
    private final PlayerService playerService;

    public GameService(GameRepository gameRepository, DeckService deckService, PlayerService playerService) {
        this.gameRepository = gameRepository;
        this.deckService = deckService;
        this.playerService = playerService;
    }

    public Mono<Game> createGame(String playerName) {

        Game game = new Game();
        game.setPlayerName(playerName);
        game.setCreatedAt(Instant.now());
        game.setStatus(GameStatus.IN_PROGRESS);


        game.setDeck(deckService.createNewDeck());

        Hand playerHand = new Hand();
        Hand dealerHand = new Hand();

        playerHand.addCard(game.drawFromDeck());
        playerHand.addCard(game.drawFromDeck());
        dealerHand.addCard(game.drawFromDeck());
        dealerHand.addCard(game.drawFromDeck());

        game.setPlayerHand(playerHand);
        game.setDealerHand(dealerHand);

        return gameRepository.save(game);
    }

    public Mono<Game> play(String gameId, MoveType move) {

        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)))
                .flatMap(game -> {

                    if (game.getStatus() != GameStatus.IN_PROGRESS) {
                        return Mono.error(new InvalidGameStateException("Game already finished"));
                    }

                    if (move == MoveType.HIT) {
                        game.getPlayerHand().addCard(game.drawFromDeck());

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
            game.getDealerHand().addCard(game.drawFromDeck());
        }
    }

    private void resolveGame(Game game) {

        Long playerId = Long.valueOf(game.getPlayerId());

        playerService.recordGamePlayed(playerId);

        if (game.getDealerHand().isBust()) {
            game.setStatus(GameStatus.DEALER_BUST);
            playerService.recordWin(playerId);
            return;
        }

        int playerScore = game.getPlayerHand().getScore();
        int dealerScore = game.getDealerHand().getScore();

        if (playerScore > dealerScore) {
            game.setStatus(GameStatus.PLAYER_WIN);
            playerService.recordWin(playerId);

        } else if (dealerScore > playerScore) {
            game.setStatus(GameStatus.DEALER_WIN);
        } else {
            game.setStatus(GameStatus.PUSH);
        }

    }

    public Mono<Game> getGameById(String gameId) {
        return gameRepository.findById(gameId)
                .switchIfEmpty(Mono.error(new GameNotFoundException(gameId)));
    }

    public Mono<Void> deleteGameById(String gameId) {
        return gameRepository.existsById(gameId)
                .flatMap(exists -> exists
                        ? gameRepository.deleteById(gameId)
                        : Mono.error(new GameNotFoundException(gameId)));
    }
}

