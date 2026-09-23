package com.gilcohen.payplus.di

import com.gilcohen.payplus.BuildConfig
import com.gilcohen.payplus.data.remote.BillingApi
import com.gilcohen.payplus.data.repository.BillingRepository
import com.gilcohen.payplus.data.repository.DefaultBillingRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/** Manual dependency container, created once per process by Gil Cohen. */
class AppContainer(baseUrl: String = BuildConfig.BASE_URL) {

    private val json = Json { ignoreUnknownKeys = true }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            }
        )
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()

    val billingRepository: BillingRepository by lazy {
        DefaultBillingRepository(retrofit.create(BillingApi::class.java))
    }
}
