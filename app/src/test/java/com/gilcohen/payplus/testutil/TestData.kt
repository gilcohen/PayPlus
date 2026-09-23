package com.gilcohen.payplus.testutil

import com.gilcohen.payplus.domain.model.BillingDetails
import com.gilcohen.payplus.domain.model.BillingHeader
import com.gilcohen.payplus.domain.model.BillingSource
import com.gilcohen.payplus.domain.model.BillingStatus
import com.gilcohen.payplus.domain.model.Currency

fun billingHeader(
    id: Long = 5165,
    cardType: String = "Visa",
) = BillingHeader(
    id = id,
    price = 14.4,
    createdAtMillis = 1_661_083_200_000,
    entryNumber = 3,
    totalEntryCount = 6,
    source = BillingSource.TERMINAL,
    currency = Currency.ILS,
    cardType = cardType,
)

fun billingDetails(
    id: Long = 5165,
    price: Double = 100.0,
    amountPaid: Double = 40.0,
) = BillingDetails(
    id = id,
    price = price,
    createdAtMillis = 1_661_083_200_000,
    entryNumber = 3,
    totalEntryCount = 6,
    currency = Currency.ILS,
    amountPaid = amountPaid,
    status = BillingStatus.PASSED,
    cardNumber = "4580123412342345",
    cardType = "Visa",
    issuer = "Max",
    source = BillingSource.TERMINAL,
    terminalName = "EMV",
    approvalNumber = "34576934",
    voucherNumber = "23-333-343",
)
