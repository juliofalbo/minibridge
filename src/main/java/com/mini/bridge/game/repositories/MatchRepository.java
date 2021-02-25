package com.mini.bridge.game.repositories;

import com.mini.bridge.game.models.Match;
import com.mini.bridge.game.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findAllByOwner(User owner);
    Match findByIdAndOwner(Long id, User owner);
    Match deleteByIdAndOwner(Long id, User owner);

}
