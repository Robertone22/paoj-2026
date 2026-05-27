package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public final class CustomCollectors {
    private CustomCollectors() {
    }

    public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
        class Accumulator {
            private final Map<String, Long> countByCountry = new HashMap<>();
            private final Map<String, Long> countByChannel = new HashMap<>();
            private final Map<String, BigDecimal> totalByAccount = new HashMap<>();
            private final Map<String, BigDecimal> totalByMonth = new HashMap<>();
            private final List<Transaction> transactions = new ArrayList<>();
            private BigDecimal totalAmount = BigDecimal.ZERO;

            private void add(Transaction transaction) {
                countByCountry.merge(transaction.getCountry(), 1L, Long::sum);
                countByChannel.merge(transaction.getChannel(), 1L, Long::sum);

                totalByAccount.merge(
                        transaction.getAccountId(),
                        transaction.getAmount(),
                        BigDecimal::add
                );

                totalByMonth.merge(
                        transaction.getMonth(),
                        transaction.getAmount(),
                        BigDecimal::add
                );

                totalAmount = totalAmount.add(transaction.getAmount());
                transactions.add(transaction);
            }

            private Accumulator combine(Accumulator other) {
                other.countByCountry.forEach((key, value) ->
                        countByCountry.merge(key, value, Long::sum)
                );

                other.countByChannel.forEach((key, value) ->
                        countByChannel.merge(key, value, Long::sum)
                );

                other.totalByAccount.forEach((key, value) ->
                        totalByAccount.merge(key, value, BigDecimal::add)
                );

                other.totalByMonth.forEach((key, value) ->
                        totalByMonth.merge(key, value, BigDecimal::add)
                );

                totalAmount = totalAmount.add(other.totalAmount);
                transactions.addAll(other.transactions);

                return this;
            }

            private Snapshot finish() {
                List<Transaction> topTransactions = transactions.stream()
                        .sorted(
                                (t1, t2) -> {
                                    int amountCompare = t2.getAmount().compareTo(t1.getAmount());

                                    if (amountCompare != 0) {
                                        return amountCompare;
                                    }

                                    return Integer.compare(t1.getId(), t2.getId());
                                }
                        )
                        .limit(topN)
                        .toList();

                return new Snapshot(
                        countByCountry,
                        countByChannel,
                        totalByAccount,
                        totalByMonth,
                        totalAmount,
                        topTransactions
                );
            }
        }

        return Collector.of(
                Accumulator::new,
                Accumulator::add,
                Accumulator::combine,
                Accumulator::finish
        );
    }
}