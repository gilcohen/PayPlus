package com.gilcohen.payplus.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BillingHeaderListResponse(
    val headers: List<BillingEntryHeaderDto> = emptyList(),
)

@Serializable
data class BillingEntryHeaderDto(
    val id: Long,
    val price: Double,
    /** Unix time in seconds. */
    val created: Long,
    val entryNumber: Int,
    val totalEntryCount: Int,
    val source: String,
    val currencyCode: String,
    val cardType: String,
)

@Serializable
data class BillingEntryDetailsResponse(
    val details: BillingEntryDetailsDto? = null,
)

@Serializable
data class BillingEntryDetailsDto(
    val id: Long,
    val price: Double,
    /** Unix time in seconds. */
    val created: Long,
    val entryNumber: Int,
    val totalEntryCount: Int,
    val currencyCode: String,
    val amountPaid: Double,
    val status: String,
    val cardNumber: String,
    val cardType: String,
    val issuer: String,
    val source: String,
    val terminalName: String,
    val approvalNumber: String,
    val voucherNumber: String,
)

@Serializable
data class DeleteBillingEntryResponse(
    /** `0` on success. */
    val status: Int,
)
