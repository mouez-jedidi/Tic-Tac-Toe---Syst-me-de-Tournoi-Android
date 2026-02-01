package com.moez.jeuxo;

import java.io.Serializable;

public class TournoiData implements Serializable {
    private static final long serialVersionUID = 1L;

    private int scoreX;
    private int scoreO;
    private int partiesNulles;
    private int totalParties;
    private String vainqueur;

    public TournoiData(int scoreX, int scoreO, int partiesNulles, int totalParties, String vainqueur) {
        this.scoreX = scoreX;
        this.scoreO = scoreO;
        this.partiesNulles = partiesNulles;
        this.totalParties = totalParties;
        this.vainqueur = vainqueur;
    }

    // Getters
    public int getScoreX() {
        return scoreX;
    }

    public int getScoreO() {
        return scoreO;
    }

    public int getPartiesNulles() {
        return partiesNulles;
    }

    public int getTotalParties() {
        return totalParties;
    }

    public String getVainqueur() {
        return vainqueur;
    }
}