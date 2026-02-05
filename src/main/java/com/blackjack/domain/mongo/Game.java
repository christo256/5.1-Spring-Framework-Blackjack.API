package com.blackjack.domain.mongo;

import com.blackjack.exception.DeckEmptyException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "games")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    private String id;

    private String playerId;
    private String playerName;

    private Hand playerHand;
    private Hand dealerHand;

    private List<Card> deck = new ArrayList<>();

    private GameStatus status;
    private Instant createdAt;

    public Card drawFromDeck() {
        if (deck == null || deck.isEmpty()) {
            throw new DeckEmptyException();
        }
        return deck.remove(0);
    }
}
