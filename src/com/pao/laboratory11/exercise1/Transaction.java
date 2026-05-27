package com.pao.laboratory11.exercise1;

import java.time.LocalDate;

public class Transaction {
    private final int id;
    private final double amount;
    private final LocalDate date;
    private final String country;
    private final String channel;

    public Transaction(int id, double amount, LocalDate date, String country, String channel) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.country = country;
        this.channel = channel;
    }

    public int getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public String getChannel() {
        return channel;
    }
}