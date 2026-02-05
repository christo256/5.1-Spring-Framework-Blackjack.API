package com.blackjack.blackjack.controller;

import com.blackjack.blackjack.dto.CreateGameRequest;
import com.blackjack.blackjack.dto.GameResponse;
import com.blackjack.blackjack.dto.PlayGameRequest;
import com.blackjack.blackjack.mapper.GameMapper;
import com.blackjack.blackjack.service.game.GameService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/game")
public class GameController {

    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @Operation(summary = "Create new Blackjack game")
    @ApiResponse(responseCode = "201", description = "Game created successfully")
    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameResponse> createGame(@Valid @RequestBody CreateGameRequest request) {
        return gameService.createGame(request.playerName())
                .map(GameMapper::toResponse);
    }

    @Operation(summary = "Get game details by ID")
    @ApiResponse(responseCode = "200", description = "Game retrieved successfully")
    @GetMapping("/{id}")
    public Mono<GameResponse> getGameById(@PathVariable String id) {
        return gameService.getGameById(id)
                .map(GameMapper::toResponse);
    }

    @Operation(summary = "Delete a game")
    @ApiResponse(responseCode = "204", description = "Game deleted successfully")
    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGameById(id);
    }


    @Operation(summary = "Play a move in a Blackjack game")
    @ApiResponse(responseCode = "200", description = "Move executed successfully")
    @PostMapping("/{id}/play")
    public Mono<GameResponse> play(
            @PathVariable String id,
            @RequestBody PlayGameRequest request
    ) {
        return gameService.play(id, request.move())
                .map(GameMapper::toResponse);
    }
}