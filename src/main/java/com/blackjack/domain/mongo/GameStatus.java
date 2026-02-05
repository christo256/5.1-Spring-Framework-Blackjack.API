package com.blackjack.domain.mongo;

public enum GameStatus {

    IN_PROGRESS,
    PLAYER_BLACKJACK,
    DEALER_BLACKJACK,
    PLAYER_BUST,
    DEALER_BUST,
    PLAYER_WIN,
    DEALER_WIN,
    PUSH
}
