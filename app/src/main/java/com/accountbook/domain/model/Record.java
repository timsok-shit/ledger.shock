package com.accountbook.domain.model;

public abstract class Record {
    protected long id;
    protected double amount;
    protected String date;
    protected String category;
    protected String note;
    protected long createdAt;

    protected Record() {}

    protected Record(long id, double amount, String date, String category, String note, long createdAt) {
        this.id = id;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.note = note;
        this.createdAt = createdAt;
    }

    public abstract double getVal();
    public abstract String getType();

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}
