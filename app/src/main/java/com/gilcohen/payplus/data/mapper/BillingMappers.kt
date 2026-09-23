package com.gilcohen.payplus.data.mapper

import com.gilcohen.payplus.data.remote.dto.BillingEntryDetailsDto
import com.gilcohen.payplus.data.remote.dto.BillingEntryHeaderDto
import com.gilcohen.payplus.domain.model.BillingDetails
import com.gilcohen.payplus.domain.model.BillingHeader
import com.gilcohen.payplus.domain.model.BillingSource
import com.gilcohen.payplus.domain.model.BillingStatus
import com.gilcohen.payplus.domain.model.Currency
import java.util.concurrent.TimeUnit

fun BillingEntryHeaderDto.toDomain() = BillingHeader(
    id = id,
    price = price,
    createdAtMillis = TimeUnit.SECONDS.toMillis(created),
    entryNumber = entryNumber,
    totalEntryCount = totalEntryCount,
    source = BillingSource.fromApi(source),
    currency = Currency.fromApi(currencyCode),
    cardType = cardType,
)

fun BillingEntryDetailsDto.toDomain() = BillingDetails(
    id = id,
    price = price,
    createdAtMillis = TimeUnit.SECONDS.toMillis(created),
    entryNumber = entryNumber,
    totalEntryCount = totalEntryCount,
    currency = Currency.fromApi(currencyCode),
    amountPaid = amountPaid,
    status = BillingStatus.fromApi(status),
    cardNumber = cardNumber,
    cardType = cardType,
    issuer = issuer,
    source = BillingSource.fromApi(source),
    terminalName = terminalName,
    approvalNumber = approvalNumber,
    voucherNumber = voucherNumber,
)
