package com.gilcohen.payplus.ui.list

import com.gilcohen.payplus.testutil.FakeBillingRepository
import com.gilcohen.payplus.testutil.MainDispatcherRule
import com.gilcohen.payplus.testutil.billingHeader
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BillingListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeBillingRepository()

    @Test
    fun `starts loading and then shows the headers`() = runTest(mainDispatcherRule.testDispatcher) {
        val items = listOf(billingHeader(id = 1), billingHeader(id = 2))
        repository.headersResult = Result.success(items)

        val viewModel = BillingListViewModel(repository)
        assertEquals(BillingListUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()
        assertEquals(BillingListUiState.Success(items), viewModel.uiState.value)
    }

    @Test
    fun `shows error when loading fails`() = runTest(mainDispatcherRule.testDispatcher) {
        repository.headersResult = FakeBillingRepository.networkError()

        val viewModel = BillingListViewModel(repository)
        advanceUntilIdle()

        assertEquals(BillingListUiState.Error, viewModel.uiState.value)
    }

    @Test
    fun `load retries after an error`() = runTest(mainDispatcherRule.testDispatcher) {
        repository.headersResult = FakeBillingRepository.networkError()
        val viewModel = BillingListViewModel(repository)
        advanceUntilIdle()

        val items = listOf(billingHeader())
        repository.headersResult = Result.success(items)
        viewModel.load()
        assertEquals(BillingListUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()
        assertEquals(BillingListUiState.Success(items), viewModel.uiState.value)
        assertEquals(2, repository.getHeadersCalls)
    }
}
