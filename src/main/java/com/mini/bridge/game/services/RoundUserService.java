package com.mini.bridge.game.services;

import com.mini.bridge.game.models.RoundUser;
import com.mini.bridge.game.repositories.RoundUserRepository;
import org.springframework.stereotype.Service;

@Service
public class RoundUserService {

    private final RoundUserRepository repository;

    public RoundUserService(RoundUserRepository repository) {
        this.repository = repository;
    }

    public RoundUser save(RoundUser match) {
        return repository.save(match);
    }

    public RoundUser find(Long id) {
        return repository.findById(id).orElse(null);
    }
}
