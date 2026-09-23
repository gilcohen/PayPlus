package com.gilcohen.payplus.ui.details

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gilcohen.payplus.R
import com.gilcohen.payplus.domain.model.BillingDetails
import com.gilcohen.payplus.domain.model.BillingSource
import com.gilcohen.payplus.domain.model.BillingStatus
import com.gilcohen.payplus.domain.model.Currency
import com.gilcohen.payplus.ui.components.ErrorContent
import com.gilcohen.payplus.ui.theme.DarkGray
import com.gilcohen.payplus.ui.theme.DeletePink
import com.gilcohen.payplus.ui.theme.LightGray
import com.gilcohen.payplus.ui.theme.PayPlusTheme
import com.gilcohen.payplus.ui.util.color
import com.gilcohen.payplus.ui.util.formatAmount
import com.gilcohen.payplus.ui.util.formatDateTime
import com.gilcohen.payplus.ui.util.formatEntryNumber
import com.gilcohen.payplus.ui.util.iconRes
import com.gilcohen.payplus.ui.util.labelRes
import com.gilcohen.payplus.ui.util.ltr
import com.gilcohen.payplus.ui.util.maskCardNumber
import com.gilcohen.payplus.ui.util.priceColor

@Composable
fun BillingDetailsRoute(
    onDeleted: () -> Unit,
    viewModel: BillingDetailsViewModel = viewModel(factory = BillingDetailsViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val deleteErrorMessage = stringResource(R.string.delete_error)
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentOnDeleted by rememberUpdatedState(onDeleted)

    LaunchedEffect(viewModel, lifecycleOwner) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.events.collect { event ->
                when (event) {
                    BillingDetailsEvent.Deleted -> currentOnDeleted()
                    BillingDetailsEvent.DeleteFailed -> snackbarHostState.showSnackbar(deleteErrorMessage)
                }
            }
        }
    }

    BillingDetailsScreen(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        onRetry = viewModel::load,
        onDeleteConfirmed = viewModel::delete,
    )
}

@Composable
fun BillingDetailsScreen(
    uiState: BillingDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onRetry: () -> Unit,
    onDeleteConfirmed: () -> Unit,
) {
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            if (uiState is BillingDetailsUiState.Success) {
                DeleteButton(
                    isDeleting = uiState.isDeleting,
                    onClick = { showDeleteDialog = true },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            when (uiState) {
                BillingDetailsUiState.Loading -> CircularProgressIndicator()
                BillingDetailsUiState.Error ->
                    ErrorContent(message = stringResource(R.string.details_error), onRetry = onRetry)
                is BillingDetailsUiState.Success -> DetailsContent(details = uiState.details)
            }
        }
    }

    if (showDeleteDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                showDeleteDialog = false
                onDeleteConfirmed()
            },
            onDismiss = { showDeleteDialog = false },
        )
    }
}

@Composable
private fun DeleteButton(isDeleting: Boolean, onClick: () -> Unit) {
    FloatingActionButton(
        onClick = { if (!isDeleting) onClick() },
        shape = CircleShape,
        containerColor = DeletePink,
        contentColor = Color.White,
    ) {
        if (isDeleting) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = Color.White,
                strokeWidth = 2.dp,
            )
        } else {
            Icon(
                imageVector = Icons.Outlined.Delete,
                contentDescription = stringResource(R.string.delete),
            )
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(R.string.delete_dialog_title)) },
        text = { Text(stringResource(R.string.delete_dialog_message)) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.delete), color = DeletePink)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
private fun DetailsContent(details: BillingDetails) {
    val notAvailable = stringResource(R.string.not_available)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            // Extra bottom space so the delete button never covers the last rows.
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 96.dp),
    ) {
        Header(details = details)

        Spacer(Modifier.height(24.dp))

        // Customer name and payment type are not part of the API contract.
        DetailRow(R.string.label_status, stringResource(details.status.labelRes))
        DetailRow(R.string.label_payment_type, notAvailable)
        DetailRow(R.string.label_card_number, ltr(maskCardNumber(details.cardNumber)))
        DetailRow(R.string.label_card_type, details.cardType)
        DetailRow(R.string.label_issuer, details.issuer)
        DetailRow(R.string.label_source, stringResource(details.source.labelRes))
        DetailRow(R.string.label_terminal_name, details.terminalName)

        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = LightGray)

        DetailRow(R.string.label_amount_paid, ltr(formatAmount(details.amountPaid, details.currency)))
        DetailRow(R.string.label_remaining, ltr(formatAmount(details.remainingAmount, details.currency)))
        DetailRow(R.string.label_approval_number, details.approvalNumber)
        DetailRow(R.string.label_voucher_number, details.voucherNumber)
    }
}

/** Source tile at the start; price, entry number/date and customer name next to it. */
@Composable
private fun Header(details: BillingDetails) {
    Row(verticalAlignment = Alignment.Top) {
        Box(
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(details.source.color),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(details.source.iconRes),
                contentDescription = stringResource(details.source.labelRes),
                tint = Color.White,
                modifier = Modifier.size(48.dp),
            )
        }

        Spacer(Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = ltr(formatAmount(details.price, details.currency)),
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                color = details.status.priceColor,
            )
            Text(
                text = ltr(
                    "${formatEntryNumber(details.entryNumber, details.totalEntryCount)} · " +
                        formatDateTime(details.createdAtMillis)
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = DarkGray,
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = LightGray)
            Text(
                text = stringResource(R.string.not_available),
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = DarkGray,
            )
        }
    }
}

@Composable
private fun DetailRow(@StringRes labelRes: Int, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = stringResource(labelRes),
            style = MaterialTheme.typography.bodySmall,
            color = DarkGray,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = DarkGray,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BillingDetailsScreenPreview() {
    PayPlusTheme {
        BillingDetailsScreen(
            uiState = BillingDetailsUiState.Success(
                BillingDetails(
                    id = 1,
                    price = 14.4,
                    createdAtMillis = 1_661_083_200_000,
                    entryNumber = 3,
                    totalEntryCount = 6,
                    currency = Currency.ILS,
                    amountPaid = 10.0,
                    status = BillingStatus.PASSED,
                    cardNumber = "4580123412342345",
                    cardType = "Visa",
                    issuer = "Max",
                    source = BillingSource.TERMINAL,
                    terminalName = "EMV",
                    approvalNumber = "34576934",
                    voucherNumber = "23-333-343",
                ),
            ),
            snackbarHostState = remember { SnackbarHostState() },
            onRetry = {},
            onDeleteConfirmed = {},
        )
    }
}
