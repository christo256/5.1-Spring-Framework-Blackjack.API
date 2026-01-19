package com.blackjack.blackjack.domain.mongo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Hand {

    private List<Card> cards = new ArrayList<>();
    private int score;

    public void addCard(Card card) {
        cards.add(card);
        recalculateScore();
    }

    private void recalculateScore() {
        int total = cards.stream().mapToInt(Card::getValue).sum();

        long aces = cards.stream()
                .filter(c -> "A".equals(c.getRank()))
                .count();

        while (total > 21 && aces > 0) {
            total -= 10;
            aces--;
        }

        this.score = total;
    }

    public boolean isBust() {
        return score > 21;
    }

    public boolean isBlackjack() {
        return score == 21 && cards.size() == 2;
    }
}

