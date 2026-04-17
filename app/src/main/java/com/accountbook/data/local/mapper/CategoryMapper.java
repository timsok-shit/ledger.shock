package com.accountbook.data.local.mapper;

import com.accountbook.data.local.entity.CategoryEntity;
import com.accountbook.domain.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static Category toDomain(CategoryEntity entity) {
        if (entity == null) return null;
        return new Category(
                entity.id,
                entity.name,
                entity.type,
                entity.sortOrder,
                entity.isPreset
        );
    }

    public static CategoryEntity toEntity(Category category) {
        if (category == null) return null;
        CategoryEntity entity = new CategoryEntity();
        entity.id = category.getId();
        entity.name = category.getName();
        entity.type = category.getType();
        entity.sortOrder = category.getSortOrder();
        entity.isPreset = category.isPreset();
        return entity;
    }

    public static List<Category> toDomainList(List<CategoryEntity> entities) {
        List<Category> result = new ArrayList<>();
        if (entities == null) return result;
        for (CategoryEntity e : entities) {
            result.add(toDomain(e));
        }
        return result;
    }
}
