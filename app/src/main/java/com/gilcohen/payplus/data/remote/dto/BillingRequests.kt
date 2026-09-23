package com.gilcohen.payplus.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data object BillingListRequest

@Serializable
data class BillingEntryRequest(
    val billingId: Long,
)
