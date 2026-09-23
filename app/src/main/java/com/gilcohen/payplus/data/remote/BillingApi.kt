package com.gilcohen.payplus.data.remote

import com.gilcohen.payplus.data.remote.dto.BillingEntryDetailsResponse
import com.gilcohen.payplus.data.remote.dto.BillingEntryRequest
import com.gilcohen.payplus.data.remote.dto.BillingHeaderListResponse
import com.gilcohen.payplus.data.remote.dto.BillingListRequest
import com.gilcohen.payplus.data.remote.dto.DeleteBillingEntryResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface BillingApi {

    @POST("payment/billing/entry/headers")
    suspend fun getHeaders(@Body request: BillingListRequest = BillingListRequest): BillingHeaderListResponse

    @POST("payment/billing/entry/details")
    suspend fun getDetails(@Body request: BillingEntryRequest): BillingEntryDetailsResponse

    @POST("payment/billing/entry/delete")
    suspend fun delete(@Body request: BillingEntryRequest): DeleteBillingEntryResponse
}
