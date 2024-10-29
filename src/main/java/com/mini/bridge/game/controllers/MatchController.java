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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Controller
public class MatchController {

    private final MatchService matchService;
    private final PlayerService playerService;
    private final RoundService roundService;
    private final RoundUserService roundUserService;
    private final AuthService authService;
    private final String ACTION_RESULT = "resultados";
    private final String ACTION_SHOT = "palpites";

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
    public String start(@ModelAttribute(value = "match") MatchStart matchStart, RedirectAttributes model) {
        Match match = new Match();

        for (String playerName : matchStart.listOfPlayers()) {
            match.addPlayers(playerService.saveOrFind(new Player(playerName)));
        }

        match.setOwner(authService.getLoggedUser());

        Match save = matchService.save(match);

        Round round = createRound(match, null);

        match.addRounds(roundService.save(round));

        model.addFlashAttribute("match", save);

        model.addFlashAttribute("currentRound", round);
        model.addFlashAttribute("olderRounds", new ArrayList<>());
        model.addFlashAttribute("action", ACTION_SHOT);

        return "redirect:/match/" + save.getId();
    }

    @GetMapping("/match/{id}")
    public String byId(@PathVariable Long id, Model model) {
        Match match = matchService.find(id);
        model.addAttribute("match", match);

        boolean finalCall = model.getAttribute("winner") != null;
        Round lastRound = match.getLastRound(finalCall);

        List<Round> olderRounds = match.getOlderRounds(finalCall);
        olderRounds.sort(Comparator.comparing(Round::getIdx).reversed());
        model.addAttribute("olderRounds", olderRounds);

        if (lastRound == null) {
            Round round = match.getRounds().get(match.getRounds().size() - 1);
            RoundUser winner = round.getUserRounds().stream().reduce((roundUser, roundUser2) -> {
                if (roundUser.getScore() > roundUser2.getScore()) {
                    return roundUser;
                }

                return roundUser2;
            }).orElse(null);

            model.addAttribute("winner", winner.getPlayer().getName());
        } else {
            lastRound.setUserRounds(lastRound.getOrderedUserRounds());
        }

        model.addAttribute("currentRound", lastRound);

        return "match";
    }

    @PostMapping("/match/round")
    public String saveRound(@ModelAttribute Round currentRound, @RequestParam(required = false) String action, RedirectAttributes model) {

        Match match = currentRound.getMatch();
        List<Round> rounds = match.getRounds();

        if (!Objects.equals(action, ACTION_RESULT)) {
            Round save = roundService.save(currentRound);
            save.setUserRounds(save.getOrderedUserRounds());

            model.addFlashAttribute("currentRound", save);
            model.addFlashAttribute("olderRounds", rounds);
            model.addFlashAttribute("match", match);
            model.addFlashAttribute("action", ACTION_RESULT);
            return "redirect:/match/" + match.getId();
        } else {

            Integer wonSetCount = currentRound.getUserRounds().stream().map(RoundUser::getNumberOfSetOfCardsWon).reduce(Integer::sum).orElse(0);

            if (!currentRound.getCurrentRoundIdxInteger().equals(wonSetCount)) {
                model.addFlashAttribute("olderRounds", rounds);
                model.addFlashAttribute("match", match);
                model.addFlashAttribute("currentRound", currentRound);
                model.addFlashAttribute("action", ACTION_RESULT);

                model.addFlashAttribute("error", "O número total de chutes deve ser igual a " + currentRound.getCurrentRoundIdxInteger());
                return "redirect:/match/" + match.getId();
            }

            currentRound.getUserRounds().forEach(RoundUser::calculateScore);
            currentRound.getUserRounds().forEach(roundUserService::save);
            roundService.save(currentRound);
        }

        Integer maxNumberOfRounds = match.getMaxNumberOfRounds();
        Integer lastRoundIdx = maxNumberOfRounds + maxNumberOfRounds + 1;

        if (!lastRoundIdx.equals(rounds.size())) {
            Round newRound = createRound(match, currentRound);
            newRound.setUserRounds(newRound.getOrderedUserRounds());
            model.addFlashAttribute("currentRound", newRound);
        } else {
            Round round = rounds.get(rounds.size() - 1);

            RoundUser winner = round.getUserRounds().stream().reduce((roundUser, roundUser2) -> {
                if (roundUser.getScore() > roundUser2.getScore()) {
                    return roundUser;
                }

                return roundUser2;
            }).orElse(null);

            model.addFlashAttribute("winner", winner.getPlayer().getName());
        }

        model.addFlashAttribute("olderRounds", rounds);
        model.addFlashAttribute("match", match);
        model.addFlashAttribute("action", ACTION_SHOT);

        return "redirect:/match/" + match.getId();
    }

    @GetMapping("/match/delete/77/{id}")
    public String delete(@PathVariable Long id, Model model) {
        matchService.delete(id);
        return "redirect:/match/all";
    }

    private Round createRound(Match match, Round lastRound) {
        Round round = roundService.save(new Round(match, match.getRounds().size() + 1));

        int currentOrder = 0;
        for (Player player : match.getPlayers()) {
            Integer previousScore = 0;

            if (lastRound != null) {
                RoundUser roundByPlayer = lastRound.findRoundByPlayer(player);
                previousScore = roundByPlayer.getScore();

                if (roundByPlayer.getCurrentOrder() == 1) {
                    currentOrder = match.getPlayers().size();
                } else {
                    currentOrder = roundByPlayer.getCurrentOrder() - 1;
                }
            } else {
                currentOrder = currentOrder + 1;
            }

            RoundUser roundUser = new RoundUser(round, player, previousScore, currentOrder);
            round.addUserRounds(roundUserService.save(roundUser));
        }

        return round;
    }
}
