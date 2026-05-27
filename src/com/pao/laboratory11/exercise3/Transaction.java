package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class Transaction {
    private final int id;
    private final BigDecimal amount;
    private final LocalDate date;
    private final String country;
    private final String channel;
    private final String accountId;

    public Transaction(int id, BigDecimal amount, LocalDate date, String country, String channel, String accountId) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.country = country;
        this.channel = channel;
        this.accountId = accountId;
    }

    public int getId() {
        return id;
    }

    public BigDecimal getAmount() {
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

    public String getAccountId() {
        return accountId;
    }

    public String getMonth() {
        return date.toString().substring(0, 7);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", amount=" + amount +
                ", date=" + date +
                ", country='" + country + '\'' +
                ", channel='" + channel + '\'' +
                ", accountId='" + accountId + '\'' +
                '}';
    }
}