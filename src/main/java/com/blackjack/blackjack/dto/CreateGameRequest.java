package com.blackjack.blackjack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateGameRequest(
        @NotBlank(message = "Player name cannot be empty")
        @Size(min = 3, max = 20, message = "Player name must be between 3 and 20 characters")
        String playerName) {

}
