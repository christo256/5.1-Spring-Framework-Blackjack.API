package com.blackjack.blackjack.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Player ranking entry")
public record PlayerRankingResponse(

        @Schema(description = "Player name", example = "Christopher")
        String playerName,

        @Schema(description = "Total games played", example = "10")
        int gamesPlayed,

        @Schema(description = "Total games won", example = "7")
        int gamesWon
) {}