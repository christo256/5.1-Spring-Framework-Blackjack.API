package com.blackjack.blackjack.service;

import com.blackjack.blackjack.domain.mongo.Card;
import com.blackjack.blackjack.domain.mongo.Game;
import com.blackjack.blackjack.domain.mongo.GameStatus;
import com.blackjack.blackjack.domain.mongo.Hand;
import com.blackjack.blackjack.repository.mongo.GameRepository;
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
        return gameRepository.findById(gameId);
    }
}

