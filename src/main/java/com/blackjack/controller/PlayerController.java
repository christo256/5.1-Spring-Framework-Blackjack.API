package com.blackjack.controller;

import com.blackjack.dto.PlayerRankingResponse;
import com.blackjack.dto.UpdatePlayerNameRequest;
import com.blackjack.service.player.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/player")
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


    @Operation(summary = "Update player name")
    @PutMapping("/{id}")
    public void updatePlayerName(
            @PathVariable Long id,
            @RequestBody UpdatePlayerNameRequest request
    ) {
        playerService.updatePlayerName(id, request.name());
    }

    @Operation(summary = "Get player ranking")
    @GetMapping("/ranking")
    public List<PlayerRankingResponse> getRanking() {
        return playerService.getRanking();
    }
}

