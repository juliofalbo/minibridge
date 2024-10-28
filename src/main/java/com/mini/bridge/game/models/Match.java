package com.mini.bridge.game.models;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private User owner;

    @OneToMany(mappedBy = "match", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Round> rounds;

    @ManyToMany
    private List<Player> players;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public Match() {

    }

    public Long getId() {
        return id;
    }

    public List<Round> getRounds() {
        return rounds == null ? new ArrayList<>() : rounds;
    }

    public void addRounds(Round round) {
        if (this.rounds == null)
            this.rounds = new ArrayList<>();

        this.rounds.add(round);
    }

    public List<Player> getPlayers() {
        return players == null ? new ArrayList<>() : players;
    }

    public void addPlayers(Player player) {
        if (this.players == null)
            this.players = new ArrayList<>();

        this.players.add(player);
    }

    public Integer getMaxNumberOfRounds() {
        int value = new BigDecimal("52").divide(new BigDecimal(String.valueOf(players.size())), RoundingMode.DOWN).toBigInteger().intValue();
        if (value > 8) {
            value = 8;
        }
        return value;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Map<Player, Integer> partialScore() {
        Map<Player, Integer> partialScore = new HashMap<>();
        for (Player player : players) {
            Integer totalScore = rounds.stream().map(round -> round.findRoundByPlayer(player).getScore())
                    .reduce(Integer::sum).orElse(0);

            partialScore.put(player, totalScore);
        }

        return partialScore;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setRounds(List<Round> rounds) {
        this.rounds = rounds;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Round getLastRound() {
        List<Round> rounds = getRounds();
        if (rounds.size() == (getMaxNumberOfRounds() + getMaxNumberOfRounds() + 2)) {
            return null;
        }
        return rounds.get(rounds.size() - 1);
    }

    public List<Round> getOlderRounds() {
        List<Round> rounds = getRounds();

        if (rounds.size() == 1) {
            return new ArrayList<>();
        }
        Round lastRound = getLastRound();

        if (lastRound == null) {
            rounds.forEach(round -> round.getUserRounds().sort(Comparator.comparing(RoundUser::getScore).reversed()));
            return rounds;
        }

        List<Round> partialRounds = rounds
                .stream()
                .takeWhile(round -> !round.getId().equals(lastRound.getId()))
                .collect(Collectors.toList());

        partialRounds.forEach(round -> round.getUserRounds().sort(Comparator.comparing(RoundUser::getScore).reversed()));
        return partialRounds;
    }

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) createdAt = LocalDateTime.now();
        if (this.updatedAt == null) updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
