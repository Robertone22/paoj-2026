package com.pao.laboratory11.exercise2;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionReporter {
    private final List<ReportTransaction> transactions;

    public TransactionReporter(List<ReportTransaction> transactions) {
        this.transactions = transactions;
    }

    public void reportMonth(String month) {
        double total = transactions.stream()
                .filter(transaction -> transaction.getMonth().equals(month))
                .mapToDouble(ReportTransaction::getAmount)
                .sum();

        long count = transactions.stream()
                .filter(transaction -> transaction.getMonth().equals(month))
                .count();

        System.out.printf(Locale.US, "MONTH %s total=%.2f count=%d%n", month, total, count);
    }

    public void reportAccount(String accountId) {
        double total = transactions.stream()
                .filter(transaction -> transaction.getAccountId().equals(accountId))
                .mapToDouble(ReportTransaction::getAmount)
                .sum();

        long count = transactions.stream()
                .filter(transaction -> transaction.getAccountId().equals(accountId))
                .count();

        System.out.printf(Locale.US, "ACCOUNT %s total=%.2f count=%d%n", accountId, total, count);
    }

    public void topChannels(int k) {
        if (transactions.isEmpty()) {
            System.out.println("NONE");
            return;
        }

        if (k <= 0) {
            return;
        }

        Map<String, Long> channelCounts = transactions.stream()
                .collect(Collectors.groupingBy(
                        ReportTransaction::getChannel,
                        Collectors.counting()
                ));

        channelCounts.entrySet()
                .stream()
                .sorted(
                        Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                                .thenComparing(Map.Entry.comparingByKey())
                )
                .limit(k)
                .forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));
    }
}