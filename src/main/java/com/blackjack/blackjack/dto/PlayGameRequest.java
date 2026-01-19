package com.blackjack.blackjack.dto;

import com.blackjack.blackjack.domain.mongo.MoveType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PlayGameRequest {

    private MoveType move;
}
