package com.accountbook.data.local.mapper;

import com.accountbook.data.local.entity.RecordEntity;
import com.accountbook.domain.model.Expense;
import com.accountbook.domain.model.Income;
import com.accountbook.domain.model.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordMapper {

    public static Record toDomain(RecordEntity entity) {
        if (entity == null) return null;

        Record record;
        if ("INCOME".equals(entity.type)) {
            record = new Income();
        } else {
            record = new Expense();
        }

        record.setId(entity.id);
        record.setAmount(entity.amount);
        record.setDate(entity.date);
        record.setCategory(entity.category);
        record.setNote(entity.note);
        record.setCreatedAt(entity.createdAt);
        return record;
    }

    public static RecordEntity toEntity(Record record) {
        if (record == null) return null;

        RecordEntity entity = new RecordEntity();
        entity.id = record.getId();
        entity.type = record.getType();
        entity.amount = record.getAmount();
        entity.date = record.getDate();
        entity.category = record.getCategory();
        entity.note = record.getNote();
        entity.createdAt = record.getCreatedAt();
        return entity;
    }

    public static List<Record> toDomainList(List<RecordEntity> entities) {
        List<Record> result = new ArrayList<>();
        if (entities == null) return result;
        for (RecordEntity e : entities) {
            result.add(toDomain(e));
        }
        return result;
    }
}
