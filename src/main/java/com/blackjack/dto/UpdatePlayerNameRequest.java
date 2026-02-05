package com.blackjack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePlayerNameRequest(
        @NotNull @NotBlank
        String name) {
}
