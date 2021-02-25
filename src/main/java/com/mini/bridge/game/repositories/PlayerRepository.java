package com.mini.bridge.game.repositories;

import com.mini.bridge.game.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Player findByNameIgnoreCase(String name);

}
