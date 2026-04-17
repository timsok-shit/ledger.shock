package com.accountbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accountbook.domain.engine.CategoryMgr
import com.accountbook.domain.model.Category
import com.accountbook.domain.repository.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class CategoryUiState(
    val categories: List<Category> = emptyList(),
    val selectedCategory: String = ""
)

class CategoryViewModel(
    private val repository: CategoryRepository,
    private val categoryMgr: CategoryMgr
) : ViewModel() {

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    private var currentLoadJob: Job? = null

    init {
        loadCategories("EXPENSE")
    }

    fun loadCategories(type: String) {
        currentLoadJob?.cancel()
        currentLoadJob = viewModelScope.launch {
            repository.getByType(type).collect { categories ->
                _uiState.update { it.copy(categories = categories) }
            }
        }
    }

    suspend fun addCategory(name: String, type: String) {
        withContext(Dispatchers.IO) {
            categoryMgr.addCategory(name, type)
        }
    }

    fun selectCategory(name: String) {
        _uiState.update { it.copy(selectedCategory = name) }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteCategory(category)
            }
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.updateCategory(category)
            }
        }
    }
}
