package com.accountbook.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.accountbook.data.local.entity.CategoryEntity;

import java.util.List;

import kotlinx.coroutines.flow.Flow;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(CategoryEntity category);

    @Query("SELECT * FROM categories WHERE type = :type OR type = 'BOTH' ORDER BY sort_order")
    Flow<List<CategoryEntity>> getByType(String type);

    @Query("SELECT * FROM categories ORDER BY sort_order")
    Flow<List<CategoryEntity>> getAll();

    @Query("SELECT * FROM categories WHERE name = :name LIMIT 1")
    CategoryEntity getByName(String name);

    @Update
    void update(CategoryEntity category);

    @Delete
    void delete(CategoryEntity category);

    @Query("DELETE FROM categories WHERE name = :name AND type = :type")
    void deleteByNameAndType(String name, String type);
}
