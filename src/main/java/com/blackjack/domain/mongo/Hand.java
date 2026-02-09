package com.blackjack.domain.mongo;

import com.blackjack.domain.mongo.enums.Rank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
                .filter(card -> card.getRank() == Rank.A)
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

