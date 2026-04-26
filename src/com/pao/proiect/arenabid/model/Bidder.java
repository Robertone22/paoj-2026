package com.pao.proiect.arenabid.model;

public class Bidder extends Participant {
    private int wonAuctionsCount;

    public Bidder(int id, String name, String email) {
        super(id, name, email);
        this.wonAuctionsCount = 0;
    }

    public int getWonAuctionsCount() {
        return wonAuctionsCount;
    }

    public void setWonAuctionsCount(int wonAuctionsCount) {
        this.wonAuctionsCount = wonAuctionsCount;
    }

    public void incrementWonAuctionsCount() {
        this.wonAuctionsCount++;
    }

    @Override
    public String getRole() {
        return "BIDDER";
    }

    @Override
    public String toString() {
        return "Bidder{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", wonAuctionsCount=" + wonAuctionsCount +
                '}';
    }
}