package com.accountbook

import android.app.Application
import com.accountbook.data.local.entity.CategoryEntity
import com.accountbook.di.AppModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountApp : Application() {
    val appModule: AppModule by lazy { AppModule(this) }

    override fun onCreate() {
        super.onCreate()
        ensurePresetCategories()
    }

    private fun ensurePresetCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            val db = appModule.getDatabase()
            val dao = db.categoryDao()
            
            val existing = dao.getAll()
            existing.collect { categories ->
                if (categories.isEmpty()) {
                    val expenseCategories = listOf("餐饮", "交通", "购物", "娱乐", "医疗", "教育", "住房", "通讯")
                    val incomeCategories = listOf("工资", "奖金", "投资", "兼职", "礼金")
                    
                    expenseCategories.forEachIndexed { i, name ->
                        val entity = CategoryEntity(name, "EXPENSE", i, true)
                        dao.insert(entity)
                    }
                    
                    incomeCategories.forEachIndexed { i, name ->
                        val entity = CategoryEntity(name, "INCOME", i, true)
                        dao.insert(entity)
                    }
                }
                return@collect
            }
        }
    }
}
