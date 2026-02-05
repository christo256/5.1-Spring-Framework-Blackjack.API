package com.blackjack.blackjack.mapper;

import com.blackjack.blackjack.domain.mongo.Hand;
import com.blackjack.blackjack.dto.CardResponse;

import java.util.List;

public class HandMapper {

    private HandMapper() {
    }

    public static List<CardResponse> toCardResponses(Hand hand) {
        if (hand == null || hand.getCards() == null) {
            return List.of();
        }
        return hand.getCards()
                .stream()
                .map(CardMapper::toResponse)
                .toList();
    }
}
