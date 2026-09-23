package com.gilcohen.payplus.ui.details

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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class BillingDetailsViewModel(
    private val billingId: Long,
    private val repository: BillingRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<BillingDetailsUiState>(BillingDetailsUiState.Loading)
    val uiState: StateFlow<BillingDetailsUiState> = _uiState.asStateFlow()

    private val _events = Channel<BillingDetailsEvent>(Channel.BUFFERED)
    val events: Flow<BillingDetailsEvent> = _events.receiveAsFlow()

    private var loadJob: Job? = null

    init {
        load()
    }

    fun delete() {
        val current = _uiState.value as? BillingDetailsUiState.Success ?: return
        if (current.isDeleting) return

        _uiState.value = current.copy(isDeleting = true)
        viewModelScope.launch {
            repository.delete(billingId).fold(
                onSuccess = { _events.send(BillingDetailsEvent.Deleted) },
                onFailure = {
                    _uiState.value = current.copy(isDeleting = false)
                    _events.send(BillingDetailsEvent.DeleteFailed)
                },
            )
        }
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
                val billingId = createSavedStateHandle().toRoute<DetailsDestination>().billingId
                BillingDetailsViewModel(billingId, app.container.billingRepository)
            }
        }
    }
}
