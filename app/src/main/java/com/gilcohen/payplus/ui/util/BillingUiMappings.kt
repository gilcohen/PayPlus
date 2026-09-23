package com.gilcohen.payplus.ui.util

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.gilcohen.payplus.R
import com.gilcohen.payplus.domain.model.BillingSource
import com.gilcohen.payplus.domain.model.BillingStatus
import com.gilcohen.payplus.domain.model.Currency
import com.gilcohen.payplus.ui.theme.DarkGray
import com.gilcohen.payplus.ui.theme.ManualGray
import com.gilcohen.payplus.ui.theme.PayPlusGreen
import com.gilcohen.payplus.ui.theme.PosGreen
import com.gilcohen.payplus.ui.theme.RejectedRed

// Matches the mockup: terminal = handheld device (ic_pos), pos = card (ic_card).
@get:DrawableRes
val BillingSource.iconRes: Int
    get() = when (this) {
        BillingSource.TERMINAL -> R.drawable.ic_pos
        BillingSource.POS -> R.drawable.ic_card
        BillingSource.MANUAL, BillingSource.UNKNOWN -> R.drawable.ic_attendant
    }

val BillingSource.color: Color
    get() = when (this) {
        BillingSource.TERMINAL -> PayPlusGreen
        BillingSource.POS -> PosGreen
        BillingSource.MANUAL, BillingSource.UNKNOWN -> ManualGray
    }

@get:StringRes
val BillingSource.labelRes: Int
    get() = when (this) {
        BillingSource.TERMINAL -> R.string.source_terminal
        BillingSource.POS -> R.string.source_pos
        BillingSource.MANUAL -> R.string.source_manual
        BillingSource.UNKNOWN -> R.string.unknown_value
    }

@get:StringRes
val BillingStatus.labelRes: Int
    get() = when (this) {
        BillingStatus.PASSED -> R.string.status_passed
        BillingStatus.REJECTED -> R.string.status_rejected
        BillingStatus.UNKNOWN -> R.string.unknown_value
    }

/** Price color on the details screen: green when passed, red when rejected. */
val BillingStatus.priceColor: Color
    get() = when (this) {
        BillingStatus.PASSED -> PayPlusGreen
        BillingStatus.REJECTED -> RejectedRed
        BillingStatus.UNKNOWN -> DarkGray
    }

/** `null` when there is no icon for the currency. */
@get:DrawableRes
val Currency.iconRes: Int?
    get() = when (this) {
        Currency.USD -> R.drawable.ic_dollar
        Currency.ILS -> R.drawable.ic_ils
        Currency.UNKNOWN -> null
    }
