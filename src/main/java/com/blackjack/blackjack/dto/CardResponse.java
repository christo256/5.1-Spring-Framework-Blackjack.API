package com.blackjack.blackjack.dto;

public record CardResponse(
        String suit,
        String rank,
        int value
) {
}
