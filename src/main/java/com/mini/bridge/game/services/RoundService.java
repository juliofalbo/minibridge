package com.mini.bridge.game.services;

import com.mini.bridge.game.models.Match;
import com.mini.bridge.game.models.Round;
import com.mini.bridge.game.repositories.MatchRepository;
import com.mini.bridge.game.repositories.RoundRepository;
import org.springframework.stereotype.Service;

@Service
public class RoundService {

    private final RoundRepository repository;

    public RoundService(RoundRepository repository) {
        this.repository = repository;
    }

    public Round save(Round match) {
        return repository.save(match);
    }

    public Round find(Long id) {
        return repository.findById(id).orElse(null);
    }
}
