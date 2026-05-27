package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        List<Transaction> transactions = List.of(
                new Transaction(1, new BigDecimal("1200.00"), LocalDate.of(2026, 5, 1), "RO", "WEB", "A1"),
                new Transaction(2, new BigDecimal("300.00"), LocalDate.of(2026, 5, 2), "RO", "ATM", "A1"),
                new Transaction(3, new BigDecimal("5200.00"), LocalDate.of(2026, 5, 3), "NG", "CRYPTO", "A2"),
                new Transaction(4, new BigDecimal("5200.00"), LocalDate.of(2026, 6, 1), "RU", "WEB", "A3"),
                new Transaction(5, new BigDecimal("90.00"), LocalDate.of(2026, 6, 2), "KP", "POS", "A2"),
                new Transaction(6, new BigDecimal("750.00"), LocalDate.of(2026, 6, 3), "RO", "APP", "A1"),
                new Transaction(7, new BigDecimal("2000.00"), LocalDate.of(2026, 7, 1), "IR", "WEB", "A4")
        );

        Snapshot snapshot = transactions.stream()
                .collect(CustomCollectors.toSnapshot(3));

        System.out.println("=== SNAPSHOT TOTAL ===");
        System.out.println("Total amount: " + snapshot.getTotalAmount());

        System.out.println();
        System.out.println("=== QUERY 1: TOP TRANSACTIONS ===");
        snapshot.getTopTransactions()
                .forEach(System.out::println);

        System.out.println();
        System.out.println("=== QUERY 2: COUNT BY COUNTRY ===");
        snapshot.getCountByCountry()
                .entrySet()
                .stream()
                .sorted(
                        Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                                .thenComparing(Map.Entry.comparingByKey())
                )
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));

        System.out.println();
        System.out.println("=== QUERY 3: COUNT BY CHANNEL ===");
        snapshot.getCountByChannel()
                .entrySet()
                .stream()
                .sorted(
                        Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                                .thenComparing(Map.Entry.comparingByKey())
                )
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));

        System.out.println();
        System.out.println("=== QUERY 4: TOTAL BY ACCOUNT ===");
        snapshot.getTotalByAccount()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));

        System.out.println();
        System.out.println("=== QUERY 5: TOTAL BY MONTH ===");
        snapshot.getTotalByMonth()
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue()));

        System.out.println();
        System.out.println("=== IMMUTABILITY CHECK ===");
        try {
            snapshot.getCountByCountry().put("XX", 99L);
        } catch (UnsupportedOperationException e) {
            System.out.println("Snapshot maps are immutable.");
        }

        try {
            snapshot.getTopTransactions().add(
                    new Transaction(99, new BigDecimal("9999.00"), LocalDate.now(), "XX", "TEST", "A99")
            );
        } catch (UnsupportedOperationException e) {
            System.out.println("Snapshot lists are immutable.");
        }
    }
}