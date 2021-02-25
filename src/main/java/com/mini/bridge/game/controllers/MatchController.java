package com.mini.bridge.game.controllers;

import com.mini.bridge.game.dtos.MatchStart;
import com.mini.bridge.game.models.Match;
import com.mini.bridge.game.models.Player;
import com.mini.bridge.game.models.Round;
import com.mini.bridge.game.models.RoundUser;
import com.mini.bridge.game.services.MatchService;
import com.mini.bridge.game.services.PlayerService;
import com.mini.bridge.game.services.RoundService;
import com.mini.bridge.game.services.RoundUserService;
import com.mini.bridge.game.services.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class MatchController {

    private final MatchService matchService;
    private final PlayerService playerService;
    private final RoundService roundService;
    private final RoundUserService roundUserService;
    private final AuthService authService;

    public MatchController(MatchService matchService, PlayerService playerService, RoundService roundService, RoundUserService roundUserService, AuthService authService) {
        this.matchService = matchService;
        this.playerService = playerService;
        this.roundService = roundService;
        this.roundUserService = roundUserService;
        this.authService = authService;
    }

    @GetMapping("/match/all")
    public String begin(Model model) {
        model.addAttribute("match", new MatchStart());
        model.addAttribute("matches", matchService.all());
        return "matches";
    }

    @PostMapping("/match/start")
    public String start(@ModelAttribute(value = "match") MatchStart matchStart, Model model) {
        Match match = new Match();

        for (String playerName : matchStart.listOfPlayers()) {
            match.addPlayers(playerService.saveOrFind(new Player(playerName)));
        }

        match.setOwner(authService.getLoggedUser());

        Match save = matchService.save(match);

        Round round = createRound(match, null);

        match.addRounds(roundService.save(round));

        model.addAttribute("match", save);

        model.addAttribute("currentRound", round);
        model.addAttribute("olderRounds", new ArrayList<>());
        return "match";
    }

    @GetMapping("/match/{id}")
    public String byId(@PathVariable Long id, Model model) {
        Match match = matchService.find(id);
        model.addAttribute("match", match);

        Round lastRound = match.getLastRound();
        model.addAttribute("currentRound", lastRound);
        model.addAttribute("olderRounds", match.getOlderRounds());

        if (lastRound == null) {
            Round round = match.getRounds().get(match.getRounds().size() - 1);
            RoundUser winner = round.getUserRounds().stream().reduce((roundUser, roundUser2) -> {
                if (roundUser.getScore() > roundUser2.getScore()) {
                    return roundUser;
                }

                return roundUser2;
            }).orElse(null);

            model.addAttribute("winner", winner.getPlayer().getName());
        }

        return "match";
    }

    @PostMapping("/match/round")
    public String saveRound(@ModelAttribute Round currentRound, Model model) {
        Integer integer = currentRound.getUserRounds().stream().map(RoundUser::getNumberOfSetOfCardsWon).reduce(Integer::sum).orElse(0);

        Match match = currentRound.getMatch();
        List<Round> rounds = match.getRounds();

        if (!currentRound.getCurrentRoundIdxInteger().equals(integer)) {
            model.addAttribute("olderRounds", rounds);
            model.addAttribute("match", match);
            model.addAttribute("currentRound", currentRound);

            model.addAttribute("error", "O número total de chutes deve ser igual a " + currentRound.getCurrentRoundIdxInteger());
            return "match";
        }

        roundService.save(currentRound);

        Integer maxNumberOfRounds = match.getMaxNumberOfRounds();
        Integer lastRoundIdx = maxNumberOfRounds + maxNumberOfRounds + 1;

        if (!lastRoundIdx.equals(rounds.size())) {
            model.addAttribute("currentRound", createRound(match, rounds.get(rounds.size() - 1)));
        } else {
            Round round = rounds.get(rounds.size() - 1);

            RoundUser winner = round.getUserRounds().stream().reduce((roundUser, roundUser2) -> {
                if (roundUser.getScore() > roundUser2.getScore()) {
                    return roundUser;
                }

                return roundUser2;
            }).orElse(null);

            model.addAttribute("winner", winner.getPlayer().getName());
        }

        rounds.forEach(round -> round.getUserRounds().sort(Comparator.comparing(RoundUser::getScore).reversed()));
        model.addAttribute("olderRounds", rounds);
        model.addAttribute("match", match);
        return "match";
    }

    private Round createRound(Match match, Round lastRound) {
        Round round = roundService.save(new Round(match, match.getRounds().size() + 1));

        for (Player player : match.getPlayers()) {
            Integer previousScore = 0;
            if (lastRound != null) {
                previousScore = lastRound.findRoundByPlayer(player).getScore();
            }

            round.addUserRounds(roundUserService.save(new RoundUser(round, player, previousScore)));
        }
        return round;
    }

    @GetMapping("/match/delete/77/{id}")
    public String delete(@PathVariable Long id, Model model) {
        matchService.delete(id);
        return "redirect:/match/all";
    }
}
