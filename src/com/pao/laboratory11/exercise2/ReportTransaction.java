package com.pao.laboratory11.exercise2;

import com.pao.laboratory11.exercise1.Transaction;

public class ReportTransaction {
    private final Transaction transaction;
    private final String accountId;

    public ReportTransaction(Transaction transaction, String accountId) {
        this.transaction = transaction;
        this.accountId = accountId;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public int getId() {
        return transaction.getId();
    }

    public double getAmount() {
        return transaction.getAmount();
    }

    public String getMonth() {
        return transaction.getDate().toString().substring(0, 7);
    }

    public String getChannel() {
        return transaction.getChannel();
    }

    public String getAccountId() {
        return accountId;
    }
}