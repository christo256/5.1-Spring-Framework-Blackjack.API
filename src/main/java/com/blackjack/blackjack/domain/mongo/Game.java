package com.blackjack.blackjack.domain.mongo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

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

    private GameStatus status;
    private Instant createdAt;
}

