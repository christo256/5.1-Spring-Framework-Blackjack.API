package com.blackjack.service.player;

import com.blackjack.domain.mongo.GameStatus;
import org.springframework.stereotype.Service;

@Service
public class PlayerStatsService {

    private final PlayerService playerService;

    public PlayerStatsService(PlayerService playerService) {
        this.playerService = playerService;
    }

    public void updateStats(Long playerId, GameStatus result) {

        playerService.recordGamePlayed(playerId);

        if (result == GameStatus.PLAYER_WIN || result == GameStatus.DEALER_BUST) {
            playerService.recordWin(playerId);
        }
    }
}