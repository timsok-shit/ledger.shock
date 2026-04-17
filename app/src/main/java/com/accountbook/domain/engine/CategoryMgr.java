package com.accountbook.domain.engine;

import com.accountbook.domain.model.Category;
import com.accountbook.domain.repository.CategoryRepository;

public class CategoryMgr {

    private final CategoryRepository repository;

    public CategoryMgr(CategoryRepository repository) {
        this.repository = repository;
    }

    public long addCategory(String name, String type) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setPreset(false);
        category.setSortOrder(1000);
        return repository.addCategory(category);
    }
}
