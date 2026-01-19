package com.blackjack.blackjack.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    private String suit;
    private String rank;
    private int value;
}
