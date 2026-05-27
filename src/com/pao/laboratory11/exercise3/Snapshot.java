package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public final class Snapshot {
    private final Map<String, Long> countByCountry;
    private final Map<String, Long> countByChannel;
    private final Map<String, BigDecimal> totalByAccount;
    private final Map<String, BigDecimal> totalByMonth;
    private final BigDecimal totalAmount;
    private final List<Transaction> topTransactions;

    public Snapshot(
            Map<String, Long> countByCountry,
            Map<String, Long> countByChannel,
            Map<String, BigDecimal> totalByAccount,
            Map<String, BigDecimal> totalByMonth,
            BigDecimal totalAmount,
            List<Transaction> topTransactions
    ) {
        this.countByCountry = Map.copyOf(countByCountry);
        this.countByChannel = Map.copyOf(countByChannel);
        this.totalByAccount = Map.copyOf(totalByAccount);
        this.totalByMonth = Map.copyOf(totalByMonth);
        this.totalAmount = totalAmount;
        this.topTransactions = List.copyOf(topTransactions);
    }

    public Map<String, Long> getCountByCountry() {
        return countByCountry;
    }

    public Map<String, Long> getCountByChannel() {
        return countByChannel;
    }

    public Map<String, BigDecimal> getTotalByAccount() {
        return totalByAccount;
    }

    public Map<String, BigDecimal> getTotalByMonth() {
        return totalByMonth;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public List<Transaction> getTopTransactions() {
        return topTransactions;
    }
}