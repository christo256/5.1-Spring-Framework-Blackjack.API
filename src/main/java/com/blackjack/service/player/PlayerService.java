package com.blackjack.service.player;

import com.blackjack.domain.sql.Player;
import com.blackjack.dto.PlayerRankingResponse;
import com.blackjack.exception.PlayerNotFoundException;
import com.blackjack.repository.sql.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    public Player findOrCreate(String playerName) {

        return playerRepository.findByName(playerName)
                .orElseGet(() -> {
                    Player newPlayer = new Player(playerName);
                    return playerRepository.save(newPlayer);
                });
    }

    public void updatePlayerName(Long id, String newName) {
        Player player = playerRepository.findById(id)
                .orElseThrow(() -> new PlayerNotFoundException(id));

        player.setName(newName);
        playerRepository.save(player);
    }

    public List<PlayerRankingResponse> getRanking() {

        return playerRepository.findAll()
                .stream()
                .sorted((p1, p2) -> {

                    double rate1 = p1.getGamesPlayed() == 0 ? 0 :
                            (double) p1.getGamesWon() / p1.getGamesPlayed();

                    double rate2 = p2.getGamesPlayed() == 0 ? 0 :
                            (double) p2.getGamesWon() / p2.getGamesPlayed();

                    int compareRate = Double.compare(rate2, rate1);
                    if (compareRate != 0) return compareRate;

                    int compareWins = Integer.compare(p2.getGamesWon(), p1.getGamesWon());
                    if (compareWins != 0) return compareWins;

                    return Integer.compare(p1.getGamesPlayed(), p2.getGamesPlayed());
                })
                .map(player -> new PlayerRankingResponse(
                        player.getName(),
                        player.getGamesPlayed(),
                        player.getGamesWon()
                ))
                .toList();
    }

    public void recordGamePlayed(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

        player.setGamesPlayed(player.getGamesPlayed() + 1);
        playerRepository.save(player);
    }

    public void recordWin(Long playerId) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException(playerId));

        player.setGamesWon(player.getGamesWon() + 1);
        playerRepository.save(player);
    }

}
