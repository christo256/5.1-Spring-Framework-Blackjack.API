package com.blackjack.blackjack.service;

import com.blackjack.blackjack.domain.sql.Player;
import com.blackjack.blackjack.dto.PlayerRankingResponse;
import com.blackjack.blackjack.exception.PlayerNotFoundException;
import com.blackjack.blackjack.repository.sql.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public void updatePlayerName(Long id, String newName) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));

        player.setName(newName);
        playerRepository.save(player);
    }

    public List<PlayerRankingResponse> getRanking() {
        return playerRepository.findAll().stream()
                .map(player -> new PlayerRankingResponse(
                        player.getName(),
                        player.getGamesPlayed(),
                        player.getGamesWon()
                ))
                .toList();
    }
}
