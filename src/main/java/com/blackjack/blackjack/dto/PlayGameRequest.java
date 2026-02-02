package com.blackjack.blackjack.dto;

import com.blackjack.blackjack.domain.mongo.MoveType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlayGameRequest {

    private MoveType move;
}
