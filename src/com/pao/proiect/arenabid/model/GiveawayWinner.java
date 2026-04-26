package com.pao.proiect.arenabid.model;

import java.time.LocalDateTime;

public class GiveawayWinner {
    private Bidder winner;
    private GiveawayPrize prize;
    private LocalDateTime drawTimestamp;

    public GiveawayWinner(Bidder winner, GiveawayPrize prize, LocalDateTime drawTimestamp) {
        this.winner = winner;
        this.prize = prize;
        this.drawTimestamp = drawTimestamp;
    }

    public Bidder getWinner() {
        return winner;
    }

    public void setWinner(Bidder winner) {
        this.winner = winner;
    }

    public GiveawayPrize getPrize() {
        return prize;
    }

    public void setPrize(GiveawayPrize prize) {
        this.prize = prize;
    }

    public LocalDateTime getDrawTimestamp() {
        return drawTimestamp;
    }

    public void setDrawTimestamp(LocalDateTime drawTimestamp) {
        this.drawTimestamp = drawTimestamp;
    }

    @Override
    public String toString() {
        return "GiveawayWinner{" +
                "winner=" + winner.getName() +
                ", prize=" + prize.getName() +
                ", drawTimestamp=" + drawTimestamp +
                '}';
    }
}