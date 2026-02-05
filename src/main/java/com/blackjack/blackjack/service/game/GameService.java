package com.blackjack.blackjack.service.game;

import com.blackjack.blackjack.domain.mongo.*;
import com.blackjack.blackjack.domain.sql.Player;
import com.blackjack.blackjack.repository.mongo.GameRepository;
import com.blackjack.blackjack.exception.GameNotFoundException;
import com.blackjack.blackjack.exception.InvalidGameStateException;
import com.blackjack.blackjack.service.player.PlayerService;
import com.blackjack.blackjack.service.player.PlayerStatsService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final DeckService deckService;
    private final PlayerService playerService;
    private final BlackJackRulesService blackJackRulesService;
    private final PlayerStatsService playerStatsService;

    public GameService(GameRepository gameRepository, DeckService deckService, PlayerService playerService, BlackJackRulesService blackJackRulesService, PlayerStatsService playerStatsService) {
        this.gameRepository = gameRepository;
        this.deckService = deckService;
        this.playerService = playerService;
        this.blackJackRulesService = blackJackRulesService;
        this.playerStatsService = playerStatsService;
    }

    public Mono<Game> createGame(String playerName) {

        Player player = playerService.findOrCreate(playerName);

        Game game = new Game();
        game.setPlayerId(String.valueOf(player.getId()));
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

                    if (game.getPlayerId() == null) {
                        return Mono.error(new InvalidGameStateException("Game has no player assigned"));
                    }
                    Long playerId = Long.valueOf(game.getPlayerId());

                    if (move == MoveType.HIT) {

                        game.getPlayerHand().addCard(game.drawFromDeck());

                        if (game.getPlayerHand().isBust()) {
                            game.setStatus(GameStatus.PLAYER_BUST);

                            playerStatsService.updateStats(playerId, GameStatus.PLAYER_BUST);
                        }

                    } else if (move == MoveType.STAND) {

                        blackJackRulesService.playDealer(game);

                        GameStatus result = blackJackRulesService.resolveGame(game);
                        game.setStatus(result);

                        playerStatsService.updateStats(playerId, result);
                    }

                    return gameRepository.save(game);
                });
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

