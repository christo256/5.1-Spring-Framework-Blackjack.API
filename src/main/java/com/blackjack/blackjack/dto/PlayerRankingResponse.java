package com.blackjack.blackjack.dto;

public record PlayerRankingResponse(
        String playerName,
        int gamesPlayed,
        int gamesWon
) {
}

