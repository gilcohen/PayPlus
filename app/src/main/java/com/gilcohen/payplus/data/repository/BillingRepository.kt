package com.gilcohen.payplus.data.repository

import com.gilcohen.payplus.domain.model.BillingDetails
import com.gilcohen.payplus.domain.model.BillingHeader

interface BillingRepository {
    suspend fun getHeaders(): Result<List<BillingHeader>>
    suspend fun getDetails(billingId: Long): Result<BillingDetails>
    suspend fun delete(billingId: Long): Result<Unit>
}
