package com.pao.proiect.arenabid.model;

import java.time.LocalDateTime;

public class Bid {
    private int id;
    private Bidder bidder;
    private double amount;
    private LocalDateTime timestamp;

    public Bid(int id, Bidder bidder, double amount, LocalDateTime timestamp) {
        this.id = id;
        this.bidder = bidder;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bidder getBidder() {
        return bidder;
    }

    public void setBidder(Bidder bidder) {
        this.bidder = bidder;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "id=" + id +
                ", bidder=" + bidder.getName() +
                ", amount=" + amount +
                ", timestamp=" + timestamp +
                '}';
    }
}