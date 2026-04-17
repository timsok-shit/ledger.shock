package com.accountbook.domain.repository;

import com.accountbook.domain.model.Record;

import java.util.List;

import kotlinx.coroutines.flow.Flow;

public interface RecordRepository {
    Flow<List<Record>> getByMonth(String month);
    Flow<List<Record>> getByCategory(String category);
    Flow<List<Record>> getByMonthAndCategory(String month, String category);
    Flow<Double> getMonthlyTotal(String month);
    Flow<List<Record>> getAll();
    long save(Record record);
    void delete(Record record);
}
