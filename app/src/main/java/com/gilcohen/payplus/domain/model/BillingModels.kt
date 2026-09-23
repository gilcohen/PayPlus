package com.gilcohen.payplus.domain.model

private const val MASTER_CARD = "MasterCard"

data class BillingHeader(
    val id: Long,
    val price: Double,
    val createdAtMillis: Long,
    val entryNumber: Int,
    val totalEntryCount: Int,
    val source: BillingSource,
    val currency: Currency,
    val cardType: String,
) {
    val isMasterCard: Boolean get() = cardType.equals(MASTER_CARD, ignoreCase = true)
}

data class BillingDetails(
    val id: Long,
    val price: Double,
    val createdAtMillis: Long,
    val entryNumber: Int,
    val totalEntryCount: Int,
    val currency: Currency,
    val amountPaid: Double,
    val status: BillingStatus,
    val cardNumber: String,
    val cardType: String,
    val issuer: String,
    val source: BillingSource,
    val terminalName: String,
    val approvalNumber: String,
    val voucherNumber: String,
) {
    val remainingAmount: Double get() = (price - amountPaid).coerceAtLeast(0.0)
}
