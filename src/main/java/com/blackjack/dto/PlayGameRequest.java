package com.blackjack.dto;

import com.blackjack.domain.mongo.MoveType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request to play a move in Blackjack")
public record PlayGameRequest(

        @Schema(
                description = "Type of move to perform",
                example = "HIT",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Move is required")
        MoveType move
) {}

