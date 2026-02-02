package com.blackjack.blackjack.dto;

import com.blackjack.blackjack.domain.mongo.GameStatus;

import java.time.Instant;
import java.util.List;

public record GameResponse(
        String id,
        String playerName,
        List<CardResponse> playerCards,
        int playerScore,
        List<CardResponse> dealerCards,
        int dealerScore,
        GameStatus status,
        Instant createdAt
) {
}


