package com.accountbook.domain.model;

public class Category {
    private long id;
    private String name;
    private String type;
    private int sortOrder;
    private boolean isPreset;

    public Category() {}

    public Category(long id, String name, String type, int sortOrder, boolean isPreset) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.sortOrder = sortOrder;
        this.isPreset = isPreset;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }

    public boolean isPreset() { return isPreset; }
    public void setPreset(boolean preset) { isPreset = preset; }
}
