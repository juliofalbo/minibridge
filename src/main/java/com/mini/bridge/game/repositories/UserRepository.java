package com.mini.bridge.game.repositories;

import com.mini.bridge.game.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String usernamer);

}
