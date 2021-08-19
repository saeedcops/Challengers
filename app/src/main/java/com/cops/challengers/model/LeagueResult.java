package com.cops.challengers.model;

public class LeagueResult {


    private int prize;
    private String league;
    private int coins;
    private int rank;
    private String nextLeague;
    private String image;
    private String token;



    public LeagueResult() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPrize() {
        return prize;
    }

    public void setPrize(int prize) {
        this.prize = prize;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getNextLeague() {
        return nextLeague;
    }

    public void setNextLeague(String nextLeague) {
        this.nextLeague = nextLeague;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
