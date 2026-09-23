package com.gilcohen.payplus.data.repository

import com.gilcohen.payplus.data.remote.BillingApi
import com.gilcohen.payplus.data.remote.dto.BillingEntryDetailsResponse
import com.gilcohen.payplus.data.remote.dto.BillingEntryHeaderDto
import com.gilcohen.payplus.data.remote.dto.BillingEntryRequest
import com.gilcohen.payplus.data.remote.dto.BillingHeaderListResponse
import com.gilcohen.payplus.data.remote.dto.BillingListRequest
import com.gilcohen.payplus.data.remote.dto.DeleteBillingEntryResponse
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.IOException

class DefaultBillingRepositoryTest {

    private val api = FakeBillingApi()

    private fun TestScope.repository() =
        DefaultBillingRepository(api, ioDispatcher = StandardTestDispatcher(testScheduler))

    @Test
    fun `getHeaders maps the response`() = runTest {
        api.headers = BillingHeaderListResponse(listOf(headerDto(5165), headerDto(5166)))

        val result = repository().getHeaders()

        assertEquals(listOf(5165L, 5166L), result.getOrThrow().map { it.id })
    }

    @Test
    fun `getHeaders returns failure when the call throws`() = runTest {
        api.error = IOException("No connection")

        val result = repository().getHeaders()

        assertTrue(result.exceptionOrNull() is IOException)
    }

    @Test
    fun `getDetails sends the billing id`() = runTest {
        repository().getDetails(5171)

        assertEquals(BillingEntryRequest(5171), api.lastRequest)
    }

    @Test
    fun `getDetails returns failure when the server has no such entry`() = runTest {
        api.details = BillingEntryDetailsResponse(details = null)

        val result = repository().getDetails(1)

        assertTrue(result.exceptionOrNull() is NoSuchElementException)
    }

    @Test
    fun `delete succeeds only when status is 0`() = runTest {
        api.deleteStatus = 0
        assertTrue(repository().delete(5165).isSuccess)
        assertEquals(BillingEntryRequest(5165), api.lastRequest)

        api.deleteStatus = -1
        assertTrue(repository().delete(5165).isFailure)
    }

    private fun headerDto(id: Long) = BillingEntryHeaderDto(
        id = id,
        price = 14.4,
        created = 1_661_083_200,
        entryNumber = 3,
        totalEntryCount = 6,
        source = "Terminal",
        currencyCode = "ILS",
        cardType = "Visa",
    )

    private class FakeBillingApi : BillingApi {
        var headers = BillingHeaderListResponse()
        var details = BillingEntryDetailsResponse()
        var deleteStatus = 0
        var error: Exception? = null
        var lastRequest: BillingEntryRequest? = null

        override suspend fun getHeaders(request: BillingListRequest): BillingHeaderListResponse {
            error?.let { throw it }
            return headers
        }

        override suspend fun getDetails(request: BillingEntryRequest): BillingEntryDetailsResponse {
            lastRequest = request
            error?.let { throw it }
            return details
        }

        override suspend fun delete(request: BillingEntryRequest): DeleteBillingEntryResponse {
            lastRequest = request
            error?.let { throw it }
            return DeleteBillingEntryResponse(deleteStatus)
        }
    }
}
