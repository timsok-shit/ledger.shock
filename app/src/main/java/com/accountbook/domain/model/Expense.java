package com.accountbook.domain.model;

public class Expense extends Record {

    public Expense() {}

    public Expense(long id, double amount, String date, String category, String note, long createdAt) {
        super(id, amount, date, category, note, createdAt);
    }

    @Override
    public double getVal() {
        return -amount;
    }

    @Override
    public String getType() {
        return "EXPENSE";
    }
}
