package com.pao.laboratory11.exercise1;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class FraudRuleEngine {
    private static final Set<String> HIGH_RISK_COUNTRIES = Set.of("RU", "NG", "IR", "KP", "SY");

    private static final Map<String, Integer> CHANNEL_SCORE = Map.of(
            "WEB", 15,
            "APP", 10,
            "CRYPTO", 30,
            "POS", 5,
            "ATM", 0
    );

    private static final int FLAG_THRESHOLD = 60;

    public static final Predicate<Transaction> amountOverThreshold =
            transaction -> transaction.getAmount() >= 1000;

    public static final Predicate<Transaction> countryInRisk =
            transaction -> HIGH_RISK_COUNTRIES.contains(transaction.getCountry());

    public static final Predicate<Transaction> channelSuspicious =
            transaction -> Set.of("WEB", "APP", "CRYPTO").contains(transaction.getChannel());

    public static final Predicate<Transaction> flaggedPredicate =
            amountOverThreshold.or(countryInRisk).or(channelSuspicious);

    public static final Comparator<Transaction> riskComparator =
            Comparator.comparingInt(FraudRuleEngine::calculateScore)
                    .reversed()
                    .thenComparingInt(Transaction::getId);

    public static int calculateScore(Transaction transaction) {
        return amountScore(transaction) + countryScore(transaction) + channelScore(transaction);
    }

    public static Verdict getVerdict(Transaction transaction) {
        return calculateScore(transaction) >= FLAG_THRESHOLD ? Verdict.FLAG : Verdict.ALLOW;
    }

    public static boolean isFlagged(Transaction transaction) {
        return getVerdict(transaction) == Verdict.FLAG;
    }

    private static int amountScore(Transaction transaction) {
        double amount = transaction.getAmount();

        if (amount >= 5000) {
            return 70;
        }

        if (amount >= 1000) {
            return 40;
        }

        if (amount >= 500) {
            return 20;
        }

        if (amount <= 100) {
            return 5;
        }

        return 0;
    }

    private static int countryScore(Transaction transaction) {
        return HIGH_RISK_COUNTRIES.contains(transaction.getCountry()) ? 25 : 0;
    }

    private static int channelScore(Transaction transaction) {
        return CHANNEL_SCORE.getOrDefault(transaction.getChannel(), 0);
    }
}