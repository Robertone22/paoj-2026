package com.pao.laboratory11.exercise1;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = Integer.parseInt(scanner.nextLine().trim());
        List<Transaction> transactions = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String[] parts = scanner.nextLine().trim().split("\\s+");

            int id = Integer.parseInt(parts[0]);
            double amount = Double.parseDouble(parts[1]);
            LocalDate date = LocalDate.parse(parts[2]);
            String country = parts[3];
            String channel = parts[4];

            transactions.add(new Transaction(id, amount, date, country, channel));
        }

        int q = Integer.parseInt(scanner.nextLine().trim());

        for (int i = 0; i < q; i++) {
            String commandLine = scanner.nextLine().trim();
            handleCommand(commandLine, transactions);
        }
    }

    private static void handleCommand(String commandLine, List<Transaction> transactions) {
        String[] parts = commandLine.split("\\s+");
        String command = parts[0];

        switch (command) {
            case "CHECK" -> handleCheck(parts, transactions);
            case "LIST_FLAGGED" -> handleListFlagged(transactions);
            case "TOP_RISK" -> handleTopRisk(parts, transactions);
            default -> System.out.println("ERR UNKNOWN_COMMAND");
        }
    }

    private static void handleCheck(String[] parts, List<Transaction> transactions) {
        int id = Integer.parseInt(parts[1]);

        Transaction transaction = findById(transactions, id);

        if (transaction == null) {
            System.out.println("CHECK " + id + " => NOT_FOUND");
            return;
        }

        Verdict verdict = FraudRuleEngine.getVerdict(transaction);
        int score = FraudRuleEngine.calculateScore(transaction);

        System.out.println("CHECK " + id + " => " + verdict + " score=" + score);
    }

    private static void handleListFlagged(List<Transaction> transactions) {
        List<Transaction> flaggedTransactions = transactions.stream()
                .filter(FraudRuleEngine::isFlagged)
                .sorted(FraudRuleEngine.riskComparator)
                .toList();

        if (flaggedTransactions.isEmpty()) {
            System.out.println("NONE");
            return;
        }

        for (Transaction transaction : flaggedTransactions) {
            Verdict verdict = FraudRuleEngine.getVerdict(transaction);
            int score = FraudRuleEngine.calculateScore(transaction);

            System.out.println("[" + transaction.getId() + "] " + verdict + " score=" + score);
        }
    }

    private static void handleTopRisk(String[] parts, List<Transaction> transactions) {
        int k = Integer.parseInt(parts[1]);

        if (k <= 0 || transactions.isEmpty()) {
            return;
        }

        transactions.stream()
                .sorted(FraudRuleEngine.riskComparator)
                .limit(k)
                .forEach(transaction -> {
                    Verdict verdict = FraudRuleEngine.getVerdict(transaction);
                    int score = FraudRuleEngine.calculateScore(transaction);

                    System.out.println("[" + transaction.getId() + "] " + verdict + " score=" + score);
                });
    }

    private static Transaction findById(List<Transaction> transactions, int id) {
        for (Transaction transaction : transactions) {
            if (transaction.getId() == id) {
                return transaction;
            }
        }

        return null;
    }
}