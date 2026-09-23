package com.gilcohen.payplus.ui.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.gilcohen.payplus.PayPlusApp
import com.gilcohen.payplus.data.repository.BillingRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BillingListViewModel(
    private val repository: BillingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<BillingListUiState>(BillingListUiState.Loading)
    val uiState: StateFlow<BillingListUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun load() {
        loadJob?.cancel()
        _uiState.value = BillingListUiState.Loading
        loadJob = viewModelScope.launch {
            _uiState.value = repository.getHeaders().fold(
                onSuccess = { BillingListUiState.Success(it) },
                onFailure = { BillingListUiState.Error },
            )
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PayPlusApp
                BillingListViewModel(app.container.billingRepository)
            }
        }
    }
}
