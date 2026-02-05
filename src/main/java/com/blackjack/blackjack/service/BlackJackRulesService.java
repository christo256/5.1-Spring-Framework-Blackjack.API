package com.blackjack.blackjack.service;

import com.blackjack.blackjack.domain.mongo.Game;
import com.blackjack.blackjack.domain.mongo.GameStatus;
import org.springframework.stereotype.Service;

@Service
public class BlackJackRulesService {

    public void playDealer(Game game) {
        while (game.getDealerHand().getScore() < 17) {
            game.getDealerHand().addCard(game.drawFromDeck());
        }
    }

    public GameStatus resolveGame(Game game) {

        if (game.getDealerHand().isBust()) {
            return GameStatus.DEALER_BUST;
        }

        int playerScore = game.getPlayerHand().getScore();
        int dealerScore = game.getDealerHand().getScore();

        if (playerScore > dealerScore) {
            return GameStatus.PLAYER_WIN;
        } else if (dealerScore > playerScore) {
            return GameStatus.DEALER_WIN;
        } else {
            return GameStatus.PUSH;
        }
    }
}
