package com.blackjack.blackjack.domain.sql;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "players")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    private int gamesPlayed;
    private int gamesWon;

    public Player(String name) {
        this.name = name;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
    }
}
