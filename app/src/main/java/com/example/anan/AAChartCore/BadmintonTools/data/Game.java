package com.example.anan.AAChartCore.BadmintonTools.data;

public class Game {
    public Game() {}

    public Game(String player_1, String player_2, String player_3, String player_4, int score_12, int score_34, int gameDate) {
        this.player_1 = player_1;
        this.player_2 = player_2;
        this.player_3 = player_3;
        this.player_4 = player_4;
        this.score_12 = score_12;
        this.score_34 = score_34;
        this.gameDate = gameDate;
    }

    public String getPlayer_1() {
        return player_1;
    }

    public void setPlayer_1(String player_1) {
        this.player_1 = player_1;
    }

    public String getPlayer_2() {
        return player_2;
    }

    public void setPlayer_2(String player_2) {
        this.player_2 = player_2;
    }

    public String getPlayer_3() {
        return player_3;
    }

    public void setPlayer_3(String player_3) {
        this.player_3 = player_3;
    }

    public String getPlayer_4() {
        return player_4;
    }

    public void setPlayer_4(String player_4) {
        this.player_4 = player_4;
    }

    public int getScore_12() {
        return score_12;
    }

    public void setScore_12(int score_12) {
        this.score_12 = score_12;
    }

    public int getScore_34() {
        return score_34;
    }

    public void setScore_34(int score_34) {
        this.score_34 = score_34;
    }

    public int getGameDate() {
        return gameDate;
    }

    public void setGameDate(int gameDate) {
        this.gameDate = gameDate;
    }

    @Override
    public String toString() {
        return player_1 + "/" + player_2 + " " +
                score_12 + " : " + score_34 + " " +
                player_3 + "/" + player_4 + " " +
                gameDate;
    }

    String player_1;
    String player_2;
    String player_3;
    String player_4;
    int score_12;
    int score_34;
    int gameDate;
}
