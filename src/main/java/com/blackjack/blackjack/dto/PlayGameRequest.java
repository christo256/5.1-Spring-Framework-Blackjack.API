package com.blackjack.blackjack.dto;

import com.blackjack.blackjack.domain.mongo.MoveType;

public record PlayGameRequest(MoveType move) {
}
