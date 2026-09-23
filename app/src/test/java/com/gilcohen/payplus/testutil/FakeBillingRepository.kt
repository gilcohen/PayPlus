package com.gilcohen.payplus.testutil

import com.gilcohen.payplus.data.repository.BillingRepository
import com.gilcohen.payplus.domain.model.BillingDetails
import com.gilcohen.payplus.domain.model.BillingHeader
import java.io.IOException

/** In-memory [BillingRepository] whose results each test sets up front. */
class FakeBillingRepository : BillingRepository {

    var headersResult: Result<List<BillingHeader>> = Result.success(listOf(billingHeader()))
    var detailsResult: Result<BillingDetails> = Result.success(billingDetails())
    var deleteResult: Result<Unit> = Result.success(Unit)

    var getHeadersCalls = 0
        private set
    val requestedDetailsIds = mutableListOf<Long>()
    val deletedIds = mutableListOf<Long>()

    override suspend fun getHeaders(): Result<List<BillingHeader>> {
        getHeadersCalls++
        return headersResult
    }

    override suspend fun getDetails(billingId: Long): Result<BillingDetails> {
        requestedDetailsIds += billingId
        return detailsResult
    }

    override suspend fun delete(billingId: Long): Result<Unit> {
        deletedIds += billingId
        return deleteResult
    }

    companion object {
        fun networkError(): Result<Nothing> = Result.failure(IOException("No connection"))
    }
}
