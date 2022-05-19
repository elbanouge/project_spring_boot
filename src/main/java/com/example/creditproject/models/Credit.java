package com.example.creditproject.models;

import lombok.Data;

@Data
public class Credit {

    private double capital;
    private int duree;
    private double taux=7.4;
    private double mensualite;
    private double minMensualite;
    private double maxMensualite;
    private final int minDuree=12;
    private final int maxDuree=84;
    public double getCapital() {
        return capital;
    }
    public void setCapital(double capital) {
        this.capital = capital;
    }
    public int getDuree() {
        return duree;
    }
    public void setDuree(int duree) {
        this.duree = duree;
    }
    public double getTaux() {
        return taux;
    }
    public void setTaux(double taux) {
        this.taux = taux;
    }
    public double getMensualite() {
        return mensualite;
    }
    public void setMensualite(double mensualite) {
        this.mensualite = mensualite;
    }
    public int getMinDuree() {
        return minDuree;
    }
    public int getMaxDuree() {
        return maxDuree;
    }
    public double getMinMensualite() {
        return minMensualite;
    }
    public void setMinMensualite(double minMensualite) {
        this.minMensualite = minMensualite;
    }
    public double getMaxMensualite() {
        return maxMensualite;
    }
    public void setMaxMensualite(double maxMensualite) {
        this.maxMensualite = maxMensualite;
    }
}
