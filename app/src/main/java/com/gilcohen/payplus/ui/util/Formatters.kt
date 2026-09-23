package com.gilcohen.payplus.ui.util

import com.gilcohen.payplus.domain.model.Currency
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val DATE_TIME_PATTERN = "dd-MM-yyyy HH:mm"

/** Formats epoch millis as `dd-MM-yyyy HH:mm` in the device time zone. */
fun formatDateTime(epochMillis: Long, locale: Locale = Locale.US): String =
    SimpleDateFormat(DATE_TIME_PATTERN, locale).format(Date(epochMillis))

/** Formats a price with thousands separators and two decimals, without a currency symbol. */
fun formatPrice(price: Double, locale: Locale = Locale.US): String =
    String.format(locale, "%,.2f", price)

/** Price with its currency symbol, e.g. `₪14.40`. */
fun formatAmount(amount: Double, currency: Currency, locale: Locale = Locale.US): String =
    currency.symbol + formatPrice(amount, locale)

/** Shows only the last four digits, e.g. `****2345`. */
fun maskCardNumber(cardNumber: String): String =
    if (cardNumber.length <= 4) cardNumber else "****" + cardNumber.takeLast(4)

/** Billing number indicator, e.g. `3/6`. */
fun formatEntryNumber(entryNumber: Int, totalEntryCount: Int): String = "$entryNumber/$totalEntryCount"

/**
 * Wraps [text] in a left-to-right isolate so numbers, dates and times keep their order
 * inside right-to-left (Hebrew) layouts.
 */
fun ltr(text: String): String = "⁦$text⁩"
