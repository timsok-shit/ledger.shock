package com.accountbook.data.local.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.accountbook.data.local.entity.RecordEntity;

import java.util.List;

import kotlinx.coroutines.flow.Flow;

@Dao
public interface RecordDao {

    @Insert
    long insert(RecordEntity record);

    @Delete
    void delete(RecordEntity record);

    @Query("SELECT * FROM ledger WHERE date LIKE :monthPrefix || '%' ORDER BY date DESC, created_at DESC")
    Flow<List<RecordEntity>> filterByMonth(String monthPrefix);

    @Query("SELECT * FROM ledger WHERE category = :category ORDER BY date DESC, created_at DESC")
    Flow<List<RecordEntity>> filterByCategory(String category);

    @Query("SELECT * FROM ledger WHERE date LIKE :monthPrefix || '%' AND category = :category ORDER BY date DESC")
    Flow<List<RecordEntity>> filterByMonthAndCategory(String monthPrefix, String category);

    @Query("SELECT SUM(CASE WHEN type='INCOME' THEN amount ELSE -amount END) FROM ledger WHERE date LIKE :monthPrefix || '%'")
    Flow<Double> monthlyTotal(String monthPrefix);

    @Query("SELECT * FROM ledger ORDER BY date DESC, created_at DESC")
    Flow<List<RecordEntity>> getAll();

    @Query("SELECT * FROM ledger WHERE id = :id")
    RecordEntity getById(long id);
}
