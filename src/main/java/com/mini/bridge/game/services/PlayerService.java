package com.mini.bridge.game.services;

import com.mini.bridge.game.models.Player;
import com.mini.bridge.game.repositories.PlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {

    private final PlayerRepository repository;

    public PlayerService(PlayerRepository repository) {
        this.repository = repository;
    }

    public Player saveOrFind(Player player) {
        Player playerDb = repository.findByNameIgnoreCase(player.getName().trim());
        if (playerDb == null) {
            return repository.save(player);
        }

        return playerDb;
    }

    public Player find(Long id) {
        return repository.findById(id).orElse(null);
    }

}
