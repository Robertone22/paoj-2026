package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

public class Main {

    public static final class Transaction {
        private final int id;
        private final BigDecimal amount;
        private final LocalDate date;
        private final String country;
        private final String channel;

        public Transaction(int id, BigDecimal amount, LocalDate date, String country, String channel) {
            this.id = id;
            this.amount = amount;
            this.date = date;
            this.country = country;
            this.channel = channel;
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

        @Override
        public String toString() {
            return "Transaction{id=" + id +
                    ", amount=" + amount +
                    ", date=" + date +
                    ", country='" + country + '\'' +
                    ", channel='" + channel + '\'' +
                    '}';
        }
    }

    public static final class Snapshot {
        private final Map<String, Long> countByCountry;
        private final Map<String, Long> countByChannel;
        private final BigDecimal totalAmount;
        private final List<Transaction> topTransactions;

        public Snapshot(Map<String, Long> countByCountry,
                        Map<String, Long> countByChannel,
                        BigDecimal totalAmount,
                        List<Transaction> topTransactions) {
            this.countByCountry = Collections.unmodifiableMap(new HashMap<>(countByCountry));
            this.countByChannel = Collections.unmodifiableMap(new HashMap<>(countByChannel));
            this.totalAmount = totalAmount;
            this.topTransactions = List.copyOf(topTransactions);
        }

        public Map<String, Long> getCountByCountry() {
            return countByCountry;
        }

        public Map<String, Long> getCountByChannel() {
            return countByChannel;
        }

        public BigDecimal getTotalAmount() {
            return totalAmount;
        }

        public List<Transaction> getTopTransactions() {
            return topTransactions;
        }
    }

    public static final class CustomCollectors {
        public static Collector<Transaction, ?, Snapshot> toSnapshot(int topN) {
            class Agg {
                Map<String, Long> byCountry = new HashMap<>();
                Map<String, Long> byChannel = new HashMap<>();
                BigDecimal total = BigDecimal.ZERO;
                List<Transaction> all = new ArrayList<>();
            }

            return Collector.of(
                    Agg::new,
                    (agg, tx) -> {
                        agg.byCountry.merge(tx.getCountry(), 1L, Long::sum);
                        agg.byChannel.merge(tx.getChannel(), 1L, Long::sum);
                        agg.total = agg.total.add(tx.getAmount());
                        agg.all.add(tx);
                    },
                    (a, b) -> {
                        b.byCountry.forEach((k, v) -> a.byCountry.merge(k, v, Long::sum));
                        b.byChannel.forEach((k, v) -> a.byChannel.merge(k, v, Long::sum));
                        a.total = a.total.add(b.total);
                        a.all.addAll(b.all);
                        return a;
                    },
                    agg -> {
                        List<Transaction> top = agg.all.stream()
                                .sorted(Comparator
                                        .comparing(Transaction::getAmount, Comparator.reverseOrder())
                                        .thenComparingInt(Transaction::getId))
                                .limit(topN)
                                .toList();

                        return new Snapshot(agg.byCountry, agg.byChannel, agg.total, top);
                    }
            );
        }
    }

    public static void main(String[] args) {
        List<Transaction> data = List.of(
                new Transaction(1, new BigDecimal("1500.00"), LocalDate.of(2026, 5, 1), "RO", "WEB"),
                new Transaction(2, new BigDecimal("750.50"), LocalDate.of(2026, 5, 2), "RO", "APP"),
                new Transaction(3, new BigDecimal("3200.00"), LocalDate.of(2026, 5, 3), "DE", "WEB"),
                new Transaction(4, new BigDecimal("3200.00"), LocalDate.of(2026, 5, 4), "DE", "ATM"),
                new Transaction(5, new BigDecimal("120.00"), LocalDate.of(2026, 5, 5), "FR", "POS"),
                new Transaction(6, new BigDecimal("980.00"), LocalDate.of(2026, 5, 6), "RO", "POS"),
                new Transaction(7, new BigDecimal("4500.00"), LocalDate.of(2026, 5, 7), "NG", "CRYPTO"),
                new Transaction(8, new BigDecimal("1100.00"), LocalDate.of(2026, 5, 8), "RO", "WEB"),
                new Transaction(9, new BigDecimal("250.00"), LocalDate.of(2026, 5, 9), "FR", "APP"),
                new Transaction(10, new BigDecimal("800.00"), LocalDate.of(2026, 5, 10), "DE", "APP")
        );

        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(5));

        System.out.println("=== Snapshot total ===");
        System.out.println("Total amount: " + snap.getTotalAmount());

        System.out.println();
        System.out.println("=== Interogare 1: Top tranzactii ===");
        snap.getTopTransactions().forEach(System.out::println);

        System.out.println();
        System.out.println("=== Interogare 2: Tari ordonate dupa numar descrescator ===");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entry ->
                        System.out.println(entry.getKey() + " -> " + entry.getValue()));

        System.out.println();
        System.out.println("=== Interogare 3: Canale ordonate dupa numar descrescator ===");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(entry ->
                        System.out.println(entry.getKey() + " -> " + entry.getValue()));

        System.out.println();
        System.out.println("=== Interogare 4: Snapshot imutabil ===");
        System.out.println("Numar tari in snapshot: " + snap.getCountByCountry().size());
        System.out.println("Numar canale in snapshot: " + snap.getCountByChannel().size());
        System.out.println("Top size: " + snap.getTopTransactions().size());
    }
}