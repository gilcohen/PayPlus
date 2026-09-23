package com.gilcohen.payplus.ui.list

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gilcohen.payplus.R
import com.gilcohen.payplus.domain.model.BillingHeader
import com.gilcohen.payplus.ui.components.ErrorContent
import com.gilcohen.payplus.ui.theme.LightGray

@Composable
fun BillingListRoute(
    onItemClick: (billingId: Long) -> Unit,
    viewModel: BillingListViewModel = viewModel(factory = BillingListViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BillingListScreen(
        uiState = uiState,
        onItemClick = onItemClick,
        onRetry = viewModel::load,
    )
}

@Composable
fun BillingListScreen(
    uiState: BillingListUiState,
    onItemClick: (billingId: Long) -> Unit,
    onRetry: () -> Unit,
) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when (uiState) {
                BillingListUiState.Loading -> CircularProgressIndicator()
                BillingListUiState.Error ->
                    ErrorContent(message = stringResource(R.string.list_error), onRetry = onRetry)
                is BillingListUiState.Success ->
                    if (uiState.items.isEmpty()) {
                        Text(stringResource(R.string.list_empty))
                    } else {
                        BillingList(items = uiState.items, onItemClick = onItemClick)
                    }
            }
        }
    }
}

@Composable
private fun BillingList(
    items: List<BillingHeader>,
    onItemClick: (billingId: Long) -> Unit,
) {
    val shape = RoundedCornerShape(8.dp)
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clip(shape)
            .border(1.dp, LightGray, shape),
    ) {
        items(items = items, key = { it.id }) { item ->
            BillingItemRow(
                item = item,
                onClick = { onItemClick(item.id) },
                // The spec defines no upload behavior yet.
                onUploadClick = {},
            )
            HorizontalDivider(color = LightGray)
        }
    }
}
