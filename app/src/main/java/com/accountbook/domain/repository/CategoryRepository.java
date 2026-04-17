package com.accountbook.domain.repository;

import com.accountbook.domain.model.Category;

import java.util.List;

import kotlinx.coroutines.flow.Flow;

public interface CategoryRepository {
    Flow<List<Category>> getByType(String type);
    Flow<List<Category>> getAll();
    long addCategory(Category category);
    void deleteCategory(Category category);
    void updateCategory(Category category);
}
