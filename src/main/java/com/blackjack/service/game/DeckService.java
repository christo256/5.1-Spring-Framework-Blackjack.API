package com.blackjack.service.game;

import com.blackjack.domain.mongo.Card;
import com.blackjack.domain.mongo.enums.Rank;
import com.blackjack.domain.mongo.enums.Suit;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Component
public class DeckService {

    public List<Card> createNewDeck() {
        List<Card> cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(suit, rank));
            }
        }
        Collections.shuffle(cards);
        return cards;
    }
}

