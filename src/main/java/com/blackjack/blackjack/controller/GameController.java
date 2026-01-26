package com.blackjack.blackjack.controller;

import com.blackjack.blackjack.domain.mongo.Game;
import com.blackjack.blackjack.dto.CreateGameRequest;
import com.blackjack.blackjack.dto.PlayGameRequest;
import com.blackjack.blackjack.service.GameService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
    public Mono<Game> createGame(@RequestBody CreateGameRequest request) {
        return gameService.createGame(request.getPlayerName());
    }

    @GetMapping("/{id}")
    public Mono<Game> getGameById(@PathVariable String id) {
        return gameService.getGameById(id);
    }

    @DeleteMapping("/{id}/delete")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteGame(@PathVariable String id) {
        return gameService.deleteGameById(id);
    }

    @PostMapping("/{id}/play")
    public Mono<Game> play(
            @PathVariable String id,
            @RequestBody PlayGameRequest request
    ) {
        return gameService.play(id, request.getMove());
    }
}