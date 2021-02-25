package com.mini.bridge.game.repositories;

import com.mini.bridge.game.models.RoundUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoundUserRepository extends JpaRepository<RoundUser, Long> {
}
