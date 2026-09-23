package com.gilcohen.payplus.data.repository

import com.gilcohen.payplus.data.mapper.toDomain
import com.gilcohen.payplus.data.remote.BillingApi
import com.gilcohen.payplus.data.remote.dto.BillingEntryRequest
import com.gilcohen.payplus.domain.model.BillingDetails
import com.gilcohen.payplus.domain.model.BillingHeader
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

private const val DELETE_SUCCESS_STATUS = 0

class DefaultBillingRepository(
    private val api: BillingApi,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : BillingRepository {

    override suspend fun getHeaders(): Result<List<BillingHeader>> = safeCall {
        api.getHeaders().headers.map { it.toDomain() }
    }

    override suspend fun getDetails(billingId: Long): Result<BillingDetails> = safeCall {
        val details = api.getDetails(BillingEntryRequest(billingId)).details
            ?: throw NoSuchElementException("Billing entry $billingId not found")
        details.toDomain()
    }

    override suspend fun delete(billingId: Long): Result<Unit> = safeCall {
        val status = api.delete(BillingEntryRequest(billingId)).status
        check(status == DELETE_SUCCESS_STATUS) { "Delete failed with status $status" }
    }

    /** Like [runCatching], but lets coroutine cancellation propagate. */
    private suspend fun <T> safeCall(block: suspend () -> T): Result<T> = withContext(ioDispatcher) {
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
