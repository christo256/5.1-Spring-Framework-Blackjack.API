package com.blackjack.blackjack.service;

import com.blackjack.blackjack.domain.mongo.Game;
import com.blackjack.blackjack.exception.GameNotFoundException;
import com.blackjack.blackjack.repository.mongo.GameRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


import org.mockito.junit.jupiter.MockitoExtension;

    @ExtendWith(MockitoExtension.class)
    class GameServiceTest {

        @Mock
        private GameRepository gameRepository;

        @InjectMocks
        private GameService gameService;

    }

