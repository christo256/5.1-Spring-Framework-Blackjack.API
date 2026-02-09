package com.blackjack.service;

import com.blackjack.domain.sql.Player;
import com.blackjack.dto.PlayerRankingResponse;
import com.blackjack.exception.PlayerNotFoundException;
import com.blackjack.repository.sql.PlayerRepository;
import com.blackjack.service.player.PlayerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerServiceTest {

    private PlayerRepository playerRepository;
    private PlayerService playerService;

    @BeforeEach
    void setup() {
        playerRepository = mock(PlayerRepository.class);
        playerService = new PlayerService(playerRepository);
    }

    @Test
    void findOrCreate_whenPlayerExists_shouldReturnExisting() {

        Player player = new Player("Chris");
        when(playerRepository.findByName("Chris")).thenReturn(Optional.of(player));

        Player result = playerService.findOrCreate("Chris");

        assertEquals("Chris", result.getName());
        verify(playerRepository, never()).save(any());
    }

    @Test
    void findOrCreate_whenPlayerDoesNotExist_shouldCreateNew() {

        when(playerRepository.findByName("Chris")).thenReturn(Optional.empty());

        Player saved = new Player("Chris");
        when(playerRepository.save(any())).thenReturn(saved);

        Player result = playerService.findOrCreate("Chris");

        assertEquals("Chris", result.getName());
        verify(playerRepository).save(any());
    }

    @Test
    void updatePlayerName_shouldUpdate() {

        Player player = new Player("Old");
        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        playerService.updatePlayerName(1L, "New");

        assertEquals("New", player.getName());
        verify(playerRepository).save(player);
    }

    @Test
    void updatePlayerName_whenNotFound_shouldThrow() {

        when(playerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(PlayerNotFoundException.class,
                () -> playerService.updatePlayerName(1L, "New"));
    }

    @Test
    void getRanking_shouldSortPlayersCorrectly() {

        Player p1 = new Player("A");
        p1.setGamesPlayed(10);
        p1.setGamesWon(8);

        Player p2 = new Player("B");
        p2.setGamesPlayed(10);
        p2.setGamesWon(5);

        when(playerRepository.findAll()).thenReturn(List.of(p2, p1));

        List<PlayerRankingResponse> result = playerService.getRanking();

        assertEquals("A", result.get(0).playerName());
    }

    @Test
    void recordGamePlayed_shouldIncreaseCounter() {

        Player player = new Player("Chris");
        player.setGamesPlayed(3);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        playerService.recordGamePlayed(1L);

        assertEquals(4, player.getGamesPlayed());
        verify(playerRepository).save(player);
    }

    @Test
    void recordWin_shouldIncreaseWins() {

        Player player = new Player("Chris");
        player.setGamesWon(2);

        when(playerRepository.findById(1L)).thenReturn(Optional.of(player));

        playerService.recordWin(1L);

        assertEquals(3, player.getGamesWon());
        verify(playerRepository).save(player);
    }
}
