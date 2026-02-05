package com.blackjack.blackjack.controller;

import com.blackjack.blackjack.dto.CreateGameRequest;
import com.blackjack.blackjack.dto.GameResponse;
import com.blackjack.blackjack.dto.PlayGameRequest;
import com.blackjack.blackjack.mapper.GameMapper;
import com.blackjack.blackjack.service.game.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/game")
public class GameController {

    private GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<GameResponse> createGame(@RequestBody CreateGameRequest request) {
        return gameService.createGame(request.playerName())
                .map(GameMapper::toResponse);
    }

    @GetMapping("/{id}")
    public Mono<GameResponse> getGameById(@PathVariable String id) {
        return gameService.getGameById(id)
                .map(GameMapper::toResponse);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGameById(id);
    }

    @PostMapping("/{id}/play")
    public Mono<GameResponse> play(
            @PathVariable String id,
            @RequestBody PlayGameRequest request
    ) {
        return gameService.play(id, request.move())
                .map(GameMapper::toResponse);
    }
}