package com.blackjack.blackjack.repository.sql;

import com.blackjack.blackjack.domain.sql.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByName(String name);
}
