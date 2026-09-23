package com.gilcohen.payplus.ui.details

import com.gilcohen.payplus.domain.model.BillingDetails

sealed interface BillingDetailsUiState {
    data object Loading : BillingDetailsUiState
    data object Error : BillingDetailsUiState
    data class Success(val details: BillingDetails) : BillingDetailsUiState
}
