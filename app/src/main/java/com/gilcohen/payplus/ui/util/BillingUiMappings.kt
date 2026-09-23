package com.gilcohen.payplus.ui.util

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color
import com.gilcohen.payplus.R
import com.gilcohen.payplus.domain.model.BillingSource
import com.gilcohen.payplus.domain.model.Currency
import com.gilcohen.payplus.ui.theme.ManualGray
import com.gilcohen.payplus.ui.theme.PayPlusGreen
import com.gilcohen.payplus.ui.theme.PosGreen

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

/** `null` when there is no icon for the currency. */
@get:DrawableRes
val Currency.iconRes: Int?
    get() = when (this) {
        Currency.USD -> R.drawable.ic_dollar
        Currency.ILS -> R.drawable.ic_ils
        Currency.UNKNOWN -> null
    }
