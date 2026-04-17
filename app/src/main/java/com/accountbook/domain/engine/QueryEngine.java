package com.accountbook.domain.engine;

import com.accountbook.domain.model.Record;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryEngine {

    public List<Record> filterByCategory(List<Record> records, String category) {
        return records.stream()
                .filter(r -> r.getCategory().equals(category))
                .collect(Collectors.toList());
    }

    public Map<String, Double> groupByCategory(List<Record> records) {
        Map<String, Double> result = new HashMap<>();
        for (Record r : records) {
            Double current = result.get(r.getCategory());
            if (current == null) {
                result.put(r.getCategory(), r.getVal());
            } else {
                result.put(r.getCategory(), current + r.getVal());
            }
        }
        return result;
    }

    public double calculateTotal(List<Record> records) {
        return records.stream()
                .mapToDouble(Record::getVal)
                .sum();
    }

    public double calculateIncomeTotal(List<Record> records) {
        return records.stream()
                .filter(r -> "INCOME".equals(r.getType()))
                .mapToDouble(Record::getAmount)
                .sum();
    }

    public double calculateExpenseTotal(List<Record> records) {
        return records.stream()
                .filter(r -> "EXPENSE".equals(r.getType()))
                .mapToDouble(Record::getAmount)
                .sum();
    }
}
