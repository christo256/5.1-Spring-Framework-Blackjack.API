package com.blackjack.mapper;

import com.blackjack.domain.mongo.Card;
import com.blackjack.dto.CardResponse;

public class CardMapper {

    private CardMapper() {}

    public static CardResponse toResponse(Card card) {
        return new CardResponse(
                card.getSuit().name(),
                card.getRank().name(),
                card.getValue()
        );
    }
}
