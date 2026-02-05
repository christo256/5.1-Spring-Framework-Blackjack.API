package com.blackjack.blackjack.mapper;

import com.blackjack.blackjack.domain.mongo.Card;
import com.blackjack.blackjack.dto.CardResponse;

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
