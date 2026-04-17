package com.accountbook.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.accountbook.domain.engine.QueryEngine
import com.accountbook.domain.model.Expense
import com.accountbook.domain.model.Income
import com.accountbook.domain.model.Record
import com.accountbook.domain.repository.RecordRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class RecordUiState(
    val records: List<Record> = emptyList(),
    val monthlyTotal: Double = 0.0,
    val incomeTotal: Double = 0.0,
    val expenseTotal: Double = 0.0,
    val selectedMonth: String = SimpleDateFormat("yyyy-MM", Locale.getDefault()).format(Date()),
    val isLoading: Boolean = false
)

class RecordViewModel(
    private val repository: RecordRepository,
    private val queryEngine: QueryEngine
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecordUiState())
    val uiState: StateFlow<RecordUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        loadMonth(_uiState.value.selectedMonth)
    }

    fun loadMonth(month: String) {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            _uiState.update { it.copy(selectedMonth = month, isLoading = true) }
            repository.getByMonth(month).collect { records ->
                val income = queryEngine.calculateIncomeTotal(records)
                val expense = queryEngine.calculateExpenseTotal(records)
                _uiState.update {
                    it.copy(
                        records = records,
                        incomeTotal = income,
                        expenseTotal = expense,
                        monthlyTotal = income - expense,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun addRecord(type: String, amount: Double, date: String, category: String, note: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val record: Record = if (type == "INCOME") {
                    Income().apply {
                        this.amount = amount
                        this.date = date
                        this.category = category
                        this.note = note
                        this.createdAt = System.currentTimeMillis()
                    }
                } else {
                    Expense().apply {
                        this.amount = amount
                        this.date = date
                        this.category = category
                        this.note = note
                        this.createdAt = System.currentTimeMillis()
                    }
                }
                repository.save(record)
            }
        }
    }

    fun deleteRecord(record: Record) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.delete(record)
            }
        }
    }

    fun updateRecord(record: Record, type: String, amount: Double, date: String, category: String, note: String) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                record.setAmount(amount)
                record.setDate(date)
                record.setCategory(category)
                record.setNote(note)
                repository.save(record)
            }
        }
    }

    fun previousMonth() {
        val current = _uiState.value.selectedMonth
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val date = sdf.parse(current) ?: return
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, -1)
        loadMonth(sdf.format(calendar.time))
    }

    fun nextMonth() {
        val current = _uiState.value.selectedMonth
        val sdf = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val date = sdf.parse(current) ?: return
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MONTH, 1)
        loadMonth(sdf.format(calendar.time))
    }
}
