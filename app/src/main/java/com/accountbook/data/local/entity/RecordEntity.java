package com.accountbook.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "ledger")
public class RecordEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "amount")
    public double amount;

    @ColumnInfo(name = "date")
    public String date;

    @ColumnInfo(name = "category")
    public String category;

    @ColumnInfo(name = "note")
    public String note;

    @ColumnInfo(name = "created_at")
    public long createdAt;

    public RecordEntity() {}

    @Ignore
    public RecordEntity(String type, double amount, String date, String category, String note, long createdAt) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.category = category;
        this.note = note;
        this.createdAt = createdAt;
    }
}
