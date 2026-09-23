package com.gilcohen.payplus.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.gilcohen.payplus.R
import com.gilcohen.payplus.domain.model.BillingHeader
import com.gilcohen.payplus.domain.model.BillingSource
import com.gilcohen.payplus.domain.model.Currency
import com.gilcohen.payplus.ui.theme.DarkGray
import com.gilcohen.payplus.ui.theme.LightGray
import com.gilcohen.payplus.ui.theme.PayPlusGreen
import com.gilcohen.payplus.ui.theme.PayPlusTheme
import com.gilcohen.payplus.ui.util.color
import com.gilcohen.payplus.ui.util.formatDateTime
import com.gilcohen.payplus.ui.util.formatEntryNumber
import com.gilcohen.payplus.ui.util.formatPrice
import com.gilcohen.payplus.ui.util.iconRes
import com.gilcohen.payplus.ui.util.ltr

/**
 * One billing entry. Laid out start-to-end (right-to-left in Hebrew, matching the mockup):
 * source tile, price + indicators, then currency and arrow at the end.
 */
@Composable
fun BillingItemRow(
    item: BillingHeader,
    onClick: () -> Unit,
    onUploadClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SourceTile(source = item.source)

        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = formatPrice(item.price),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
                color = DarkGray,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (item.isMasterCard) {
                    UploadButton(onClick = onUploadClick)
                    Spacer(Modifier.width(6.dp))
                }
                Text(
                    text = ltr(
                        "${formatEntryNumber(item.entryNumber, item.totalEntryCount)} · " +
                            formatDateTime(item.createdAtMillis)
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = DarkGray,
                )
            }
        }

        Spacer(Modifier.weight(1f))

        item.currency.iconRes?.let { iconRes ->
            Image(
                painter = painterResource(iconRes),
                contentDescription = item.currency.name,
            )
        }

        Box(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .size(28.dp)
                .clip(CircleShape)
                .background(LightGray),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = DarkGray,
            )
        }
    }
}

@Composable
private fun SourceTile(source: BillingSource) {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(44.dp)
            .background(source.color),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(source.iconRes),
            contentDescription = source.name,
            tint = Color.White,
        )
    }
}

@Composable
private fun UploadButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .clip(CircleShape)
            .background(PayPlusGreen)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_upload),
            contentDescription = stringResource(R.string.upload),
            tint = Color.White,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BillingItemRowPreview() {
    PayPlusTheme {
        Column {
            BillingItemRow(
                item = BillingHeader(
                    id = 1,
                    price = 14.4,
                    createdAtMillis = 1_661_083_200_000,
                    entryNumber = 3,
                    totalEntryCount = 6,
                    source = BillingSource.TERMINAL,
                    currency = Currency.ILS,
                    cardType = "Visa",
                ),
                onClick = {},
                onUploadClick = {},
            )
            BillingItemRow(
                item = BillingHeader(
                    id = 2,
                    price = 25357.53,
                    createdAtMillis = 1_661_083_200_000,
                    entryNumber = 1,
                    totalEntryCount = 2,
                    source = BillingSource.MANUAL,
                    currency = Currency.USD,
                    cardType = "MasterCard",
                ),
                onClick = {},
                onUploadClick = {},
            )
        }
    }
}
