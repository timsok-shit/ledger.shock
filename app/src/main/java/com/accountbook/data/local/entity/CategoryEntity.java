package com.accountbook.data.local.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "categories",
        indices = {@Index(value = {"name", "type"}, unique = true)}
)
public class CategoryEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "type")
    public String type;

    @ColumnInfo(name = "sort_order")
    public int sortOrder;

    @ColumnInfo(name = "is_preset")
    public boolean isPreset;

    public CategoryEntity() {}

    @Ignore
    public CategoryEntity(String name, String type, int sortOrder, boolean isPreset) {
        this.name = name;
        this.type = type;
        this.sortOrder = sortOrder;
        this.isPreset = isPreset;
    }
}
