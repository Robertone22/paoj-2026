package com.pao.laboratory11.exercise2;

import com.pao.laboratory11.exercise1.Transaction;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String firstLine = readNonEmptyLine(scanner);
        if (firstLine == null) {
            return;
        }

        int n = Integer.parseInt(firstLine);
        List<ReportTransaction> transactions = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String line = readNonEmptyLine(scanner);
            if (line == null) {
                return;
            }

            String[] parts = line.split("\\s+");

            int id = Integer.parseInt(parts[0]);
            double amount = Double.parseDouble(parts[1]);
            LocalDate date = LocalDate.parse(parts[2]);
            String country = parts[3];
            String channel = parts[4];
            String accountId = parts[5];

            Transaction baseTransaction = new Transaction(id, amount, date, country, channel);
            ReportTransaction reportTransaction = new ReportTransaction(baseTransaction, accountId);

            transactions.add(reportTransaction);
        }

        TransactionReporter reporter = new TransactionReporter(transactions);

        String queryCountLine = readNonEmptyLine(scanner);
        if (queryCountLine == null) {
            return;
        }

        int q = Integer.parseInt(queryCountLine);

        for (int i = 0; i < q; i++) {
            String commandLine = readNonEmptyLine(scanner);
            if (commandLine == null) {
                return;
            }

            handleCommand(commandLine, reporter);
        }
    }

    private static void handleCommand(String commandLine, TransactionReporter reporter) {
        String[] parts = commandLine.split("\\s+");
        String command = parts[0];

        switch (command) {
            case "REPORT_MONTH" -> reporter.reportMonth(parts[1]);
            case "REPORT_ACCOUNT" -> reporter.reportAccount(parts[1]);
            case "TOP_CHANNELS" -> reporter.topChannels(Integer.parseInt(parts[1]));
            default -> {
                // Unknown commands are ignored, as required by the checker.
            }
        }
    }

    private static String readNonEmptyLine(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            if (!line.isEmpty()) {
                return line;
            }
        }

        return null;
    }
}