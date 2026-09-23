package com.gilcohen.payplus.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.gilcohen.payplus.PayPlusApp
import com.gilcohen.payplus.data.repository.BillingRepository
import com.gilcohen.payplus.ui.navigation.DetailsDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BillingDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: BillingRepository,
) : ViewModel() {

    private val billingId: Long = savedStateHandle.toRoute<DetailsDestination>().billingId

    private val _uiState = MutableStateFlow<BillingDetailsUiState>(BillingDetailsUiState.Loading)
    val uiState: StateFlow<BillingDetailsUiState> = _uiState.asStateFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun load() {
        loadJob?.cancel()
        _uiState.value = BillingDetailsUiState.Loading
        loadJob = viewModelScope.launch {
            _uiState.value = repository.getDetails(billingId).fold(
                onSuccess = { BillingDetailsUiState.Success(it) },
                onFailure = { BillingDetailsUiState.Error },
            )
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[APPLICATION_KEY] as PayPlusApp
                BillingDetailsViewModel(createSavedStateHandle(), app.container.billingRepository)
            }
        }
    }
}
