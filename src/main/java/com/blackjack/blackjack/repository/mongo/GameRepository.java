package com.blackjack.blackjack.repository.mongo;

import com.blackjack.blackjack.domain.mongo.Game;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface GameRepository extends ReactiveMongoRepository<Game, String> {
}
