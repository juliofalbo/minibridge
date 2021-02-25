package com.mini.bridge.game.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "rounds")
public class Round {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Match match;

    @OneToMany(mappedBy = "round", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<RoundUser> userRounds;

    private Integer idx;

    public Round() {
    }

    public Round(Match match, Integer idx) {
        this.idx = idx;
        this.match = match;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<RoundUser> getUserRounds() {
        return userRounds == null ? new ArrayList<>() : userRounds;
    }

    public void addUserRounds(RoundUser round) {
        if (this.userRounds == null)
            this.userRounds = new ArrayList<>();

        this.userRounds.add(round);
    }

    public Match getMatch() {
        return match;
    }

    public RoundUser findRoundByPlayer(Player player) {
        return userRounds.stream().filter(roundUser -> roundUser.getPlayer().getName().equals(player.getName())).findFirst().orElse(null);
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public void setUserRounds(List<RoundUser> userRounds) {
        this.userRounds = userRounds;
    }

    public Integer getIdx() {
        return idx;
    }

    public void setIdx(Integer idx) {
        this.idx = idx;
    }

    public String getCurrentRoundIdx() {
        Integer maxNumberOfRounds = match.getMaxNumberOfRounds();
        Integer idx = getIdx();
        if (idx > maxNumberOfRounds) {
            Round lastRound = match.getRounds().get(idx - 2);
            Round beforeLastRound = match.getRounds().get(idx - 3);

            if(lastRound.getIdx().equals(maxNumberOfRounds) && !beforeLastRound.getIdx().equals(maxNumberOfRounds)) {
                return (idx - 1) + " - SEM TRUNFO";
            }

            return String.valueOf(Math.abs(idx - (maxNumberOfRounds + maxNumberOfRounds + 2)));
        }

        return idx.toString();
    }

    public Integer getCurrentRoundIdxInteger() {
        return Integer.parseInt(getCurrentRoundIdx().replace(" - SEM TRUNFO", ""));
    }
}
