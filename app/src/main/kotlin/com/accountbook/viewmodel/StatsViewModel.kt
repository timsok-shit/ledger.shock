package com.accountbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accountbook.domain.engine.QueryEngine
import com.accountbook.domain.model.Record
import com.accountbook.domain.repository.RecordRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class StatsUiState(
    val records: List<Record> = emptyList(),
    val incomeTotal: Double = 0.0,
    val expenseTotal: Double = 0.0,
    val categoryBreakdown: Map<String, Double> = emptyMap(),
    val selectedMonth: String = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date())
)

class StatsViewModel(
    private val repository: RecordRepository,
    private val queryEngine: QueryEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadStats(_uiState.value.selectedMonth)
    }

    fun loadStats(month: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            repository.getByMonth(month).collect { records ->
                val income = queryEngine.calculateIncomeTotal(records)
                val expense = queryEngine.calculateExpenseTotal(records)
                val breakdown = queryEngine.groupByCategory(records.filter { it.getType() == "EXPENSE" })
                _uiState.update {
                    it.copy(
                        records = records,
                        incomeTotal = income,
                        expenseTotal = expense,
                        categoryBreakdown = breakdown,
                        selectedMonth = month
                    )
                }
            }
        }
    }
}
