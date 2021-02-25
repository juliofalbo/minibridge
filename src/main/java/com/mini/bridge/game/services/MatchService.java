package com.mini.bridge.game.services;

import com.mini.bridge.game.models.Match;
import com.mini.bridge.game.repositories.MatchRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;
    private final AuthService authService;

    public MatchService(MatchRepository matchRepository, AuthService authService) {
        this.matchRepository = matchRepository;
        this.authService = authService;
    }

    public List<Match> all(){
        return matchRepository.findAllByOwner(authService.getLoggedUser());
    }

    public Match save(Match match) {
        return matchRepository.save(match);
    }

    public Match find(Long id) {
        return matchRepository.findByIdAndOwner(id, authService.getLoggedUser());
    }

    public void delete(Long id) {
        Match match = find(id);
        if(match == null){
            throw new RuntimeException("Unauthorized");
        }
        matchRepository.deleteById(id);
    }

}
