package com.mini.bridge.game.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MatchStart {
    private String player1;
    private String player2;
    private String player3;
    private String player4;
    private String player5;
    private String player6;
    private String player7;
    private String player8;

    public String getPlayer1() {
        return player1;
    }

    public void setPlayer1(String player1) {
        this.player1 = player1;
    }

    public String getPlayer2() {
        return player2;
    }

    public void setPlayer2(String player2) {
        this.player2 = player2;
    }

    public String getPlayer3() {
        return player3;
    }

    public void setPlayer3(String player3) {
        this.player3 = player3;
    }

    public String getPlayer4() {
        return player4;
    }

    public void setPlayer4(String player4) {
        this.player4 = player4;
    }

    public String getPlayer5() {
        return player5;
    }

    public void setPlayer5(String player5) {
        this.player5 = player5;
    }

    public String getPlayer6() {
        return player6;
    }

    public void setPlayer6(String player6) {
        this.player6 = player6;
    }

    public String getPlayer7() {
        return player7;
    }

    public void setPlayer7(String player7) {
        this.player7 = player7;
    }

    public String getPlayer8() {
        return player8;
    }

    public void setPlayer8(String player8) {
        this.player8 = player8;
    }

    public List<String> listOfPlayers() {
        List<String> listOfPlayers = new ArrayList<>();

        if (isNotEmpty(this.player1)) {
            listOfPlayers.add(this.player1);
        }

        if (isNotEmpty(this.player2)) {
            listOfPlayers.add(this.player2);
        }

        if (isNotEmpty(this.player3)) {
            listOfPlayers.add(this.player3);
        }

        if (isNotEmpty(this.player4)) {
            listOfPlayers.add(this.player4);
        }

        if (isNotEmpty(this.player5)) {
            listOfPlayers.add(this.player5);
        }

        if (isNotEmpty(this.player6)) {
            listOfPlayers.add(this.player6);
        }

        if (isNotEmpty(this.player7)) {
            listOfPlayers.add(this.player7);
        }

        if (isNotEmpty(this.player8)) {
            listOfPlayers.add(this.player8);
        }

        return listOfPlayers;
    }

    private Boolean isNotEmpty(String str) {
        return str != null && !str.trim().equals("");
    }
}
