package com.blackjack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request to create a new Blackjack game")
public record CreateGameRequest(

        @Schema(
                description = "Name of the player starting the game",
                example = "Christopher",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Player name cannot be empty")
        @Size(min = 3, max = 20, message = "Player name must be between 3 and 20 characters")
        String playerName
) {}
