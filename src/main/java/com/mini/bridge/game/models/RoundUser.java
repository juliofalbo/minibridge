package com.mini.bridge.game.models;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "rounds_user")
public class RoundUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Round round;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    private Player player;

    private Integer numberOfSetOfCardsWon;

    private Integer shot;

    private Integer score;

    private Integer currentOrder;

    public RoundUser(Player player, Integer shot) {
        this.player = player;
        this.shot = shot;
    }

    public RoundUser(Round round, Player player) {
        this.player = player;
        this.round = round;
    }

    public RoundUser(Round round, Player player, Integer previousScore, Integer currentOrder) {
        this.player = player;
        this.round = round;
        this.score = previousScore;
        this.currentOrder = currentOrder;
    }

    public RoundUser() {

    }

    public Long getId() {
        return id;
    }

    public Boolean getSuccess() {
        return getShot().equals(numberOfSetOfCardsWon);
    }

    public String getSuccessTranslated() {
        return getSuccess() ? "Sim" : "Não";
    }

    public Integer getNumberOfSetOfCardsWon() {
        return numberOfSetOfCardsWon == null ? 0 : numberOfSetOfCardsWon;
    }

    public void setNumberOfSetOfCardsWon(Integer setOfCards) {
        this.numberOfSetOfCardsWon = setOfCards;
    }

    public Player getPlayer() {
        return player;
    }

    private Integer calculateRoundScore() {
        return getSuccess() ? getNumberOfSetOfCardsWon() + 10 : getNumberOfSetOfCardsWon();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Integer getShot() {
        return shot == null ? 0 : shot;
    }

    public void setShot(Integer shot) {
        this.shot = shot;
    }

    public Integer getScore() {
        return score;
    }

    public void calculateScore() {
        if(this.score == null) {
            this.score = 0;
        }

        this.score += calculateRoundScore();
    }

    public Integer getCurrentOrder() {
        return currentOrder;
    }

    public void setCurrentOrder(Integer currentOrder) {
        this.currentOrder = currentOrder;
    }
}
