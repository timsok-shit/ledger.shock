package com.accountbook.di

import android.content.Context
import com.accountbook.data.local.AppDatabase
import com.accountbook.data.local.dao.CategoryDao
import com.accountbook.data.local.dao.RecordDao
import com.accountbook.data.local.mapper.CategoryMapper
import com.accountbook.data.local.mapper.RecordMapper
import com.accountbook.domain.engine.CategoryMgr
import com.accountbook.domain.engine.QueryEngine
import com.accountbook.domain.model.Category
import com.accountbook.domain.model.Record
import com.accountbook.domain.repository.CategoryRepository
import com.accountbook.domain.repository.RecordRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AppModule(context: Context) {

    private val db: AppDatabase = AppDatabase.getDatabase(context)
    private val recordDao: RecordDao = db.recordDao()
    private val categoryDao: CategoryDao = db.categoryDao()

    val queryEngine = QueryEngine()

    fun getDatabase(): AppDatabase = db

    val recordRepository = object : RecordRepository {
        override fun getByMonth(month: String): Flow<List<Record>> =
            recordDao.filterByMonth(month).map { RecordMapper.toDomainList(it) }

        override fun getByCategory(category: String): Flow<List<Record>> =
            recordDao.filterByCategory(category).map { RecordMapper.toDomainList(it) }

        override fun getByMonthAndCategory(month: String, category: String): Flow<List<Record>> =
            recordDao.filterByMonthAndCategory(month, category).map { RecordMapper.toDomainList(it) }

        override fun getMonthlyTotal(month: String): Flow<Double> =
            recordDao.monthlyTotal(month)

        override fun getAll(): Flow<List<Record>> =
            recordDao.getAll().map { RecordMapper.toDomainList(it) }

        override fun save(record: Record): Long {
            return recordDao.insert(RecordMapper.toEntity(record))
        }

        override fun delete(record: Record) {
            val entity = RecordMapper.toEntity(record)
            entity.id = record.getId()
            recordDao.delete(entity)
        }
    }

    val categoryRepository = object : CategoryRepository {
        override fun getByType(type: String): Flow<List<Category>> =
            categoryDao.getByType(type).map { CategoryMapper.toDomainList(it) }

        override fun getAll(): Flow<List<Category>> =
            categoryDao.getAll().map { CategoryMapper.toDomainList(it) }

        override fun addCategory(category: Category): Long {
            return categoryDao.insert(CategoryMapper.toEntity(category))
        }

        override fun deleteCategory(category: Category) {
            val entity = CategoryMapper.toEntity(category)
            entity.id = category.getId()
            categoryDao.delete(entity)
        }

        override fun updateCategory(category: Category) {
            val entity = CategoryMapper.toEntity(category)
            entity.id = category.getId()
            categoryDao.update(entity)
        }
    }

    val categoryMgr = CategoryMgr(categoryRepository)
}
