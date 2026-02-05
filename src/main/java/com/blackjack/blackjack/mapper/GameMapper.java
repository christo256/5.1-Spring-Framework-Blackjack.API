package com.blackjack.blackjack.mapper;

import com.blackjack.blackjack.domain.mongo.Game;
import com.blackjack.blackjack.dto.GameResponse;

public class GameMapper {

    private GameMapper() {}

    public static GameResponse toResponse(Game game) {

        if (game == null) {
            return null;
    }

        return new GameResponse(
                game.getId(),
                game.getPlayerName(),
                HandMapper.toCardResponses(game.getPlayerHand()),
                game.getPlayerHand().getScore(),
                HandMapper.toCardResponses(game.getDealerHand()),
                game.getDealerHand().getScore(),
                game.getStatus(),
                game.getCreatedAt()
        );
    }
}
