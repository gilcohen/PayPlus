package com.gilcohen.payplus.ui.details

import com.gilcohen.payplus.domain.model.BillingDetails

sealed interface BillingDetailsUiState {
    data object Loading : BillingDetailsUiState
    data object Error : BillingDetailsUiState
    data class Success(
        val details: BillingDetails,
        val isDeleting: Boolean = false,
    ) : BillingDetailsUiState
}

/** One-shot events the screen reacts to once (navigation, snackbars). */
sealed interface BillingDetailsEvent {
    data object Deleted : BillingDetailsEvent
    data object DeleteFailed : BillingDetailsEvent
}
