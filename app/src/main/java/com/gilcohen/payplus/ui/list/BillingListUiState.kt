package com.gilcohen.payplus.ui.list

import com.gilcohen.payplus.domain.model.BillingHeader

sealed interface BillingListUiState {
    data object Loading : BillingListUiState
    data object Error : BillingListUiState
    data class Success(val items: List<BillingHeader>) : BillingListUiState
}
