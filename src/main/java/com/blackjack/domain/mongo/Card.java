package com.blackjack.domain.mongo;

import com.blackjack.domain.mongo.enums.Rank;
import com.blackjack.domain.mongo.enums.Suit;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private Suit suit;
    private Rank rank;

    public int getValue() {
        return rank.getValue();
    }

}
