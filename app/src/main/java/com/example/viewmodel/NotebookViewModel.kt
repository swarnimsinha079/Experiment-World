package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.LabDatabase
import com.example.data.local.LabRecordEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NotebookViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = LabDatabase.getDatabase(application).labRecordDao()

    val records: StateFlow<List<LabRecordEntity>> = dao.getAllRecords()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteRecord(record: LabRecordEntity) {
        viewModelScope.launch {
            dao.deleteRecord(record)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            dao.clearAllRecords()
        }
    }
}
