package com.blackjack.blackjack.dto;

import com.blackjack.blackjack.domain.mongo.MoveType;
import jakarta.validation.constraints.NotNull;

public record PlayGameRequest(
        @NotNull(message = "Move is required")
        MoveType move) {
}
