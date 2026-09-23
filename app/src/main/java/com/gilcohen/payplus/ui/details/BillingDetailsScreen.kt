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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gilcohen.payplus.R
import com.gilcohen.payplus.domain.model.BillingDetails
import com.gilcohen.payplus.domain.model.BillingSource
import com.gilcohen.payplus.domain.model.BillingStatus
import com.gilcohen.payplus.domain.model.Currency
import com.gilcohen.payplus.ui.components.ErrorContent
import com.gilcohen.payplus.ui.theme.DarkGray
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
    viewModel: BillingDetailsViewModel = viewModel(factory = BillingDetailsViewModel.Factory),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    BillingDetailsScreen(
        uiState = uiState,
        onRetry = viewModel::load,
    )
}

@Composable
fun BillingDetailsScreen(
    uiState: BillingDetailsUiState,
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
                BillingDetailsUiState.Loading -> CircularProgressIndicator()
                BillingDetailsUiState.Error ->
                    ErrorContent(message = stringResource(R.string.details_error), onRetry = onRetry)
                is BillingDetailsUiState.Success -> DetailsContent(details = uiState.details)
            }
        }
    }
}

@Composable
private fun DetailsContent(details: BillingDetails) {
    val notAvailable = stringResource(R.string.not_available)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
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
    PayPlusTheme(dynamicColor = false) {
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
            onRetry = {},
        )
    }
}
