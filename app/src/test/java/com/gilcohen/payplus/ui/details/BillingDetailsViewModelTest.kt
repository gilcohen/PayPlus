package com.gilcohen.payplus.ui.details

import com.gilcohen.payplus.testutil.FakeBillingRepository
import com.gilcohen.payplus.testutil.MainDispatcherRule
import com.gilcohen.payplus.testutil.billingDetails
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

private const val BILLING_ID = 5165L

class BillingDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = FakeBillingRepository()

    private fun TestScope.loadedViewModel(): BillingDetailsViewModel {
        val viewModel = BillingDetailsViewModel(BILLING_ID, repository)
        advanceUntilIdle()
        return viewModel
    }

    @Test
    fun `loads details for the given billing id`() = runTest(mainDispatcherRule.testDispatcher) {
        val details = billingDetails(id = BILLING_ID)
        repository.detailsResult = Result.success(details)

        val viewModel = BillingDetailsViewModel(BILLING_ID, repository)
        assertEquals(BillingDetailsUiState.Loading, viewModel.uiState.value)

        advanceUntilIdle()
        assertEquals(BillingDetailsUiState.Success(details), viewModel.uiState.value)
        assertEquals(listOf(BILLING_ID), repository.requestedDetailsIds)
    }

    @Test
    fun `shows error when loading fails and recovers on retry`() = runTest(mainDispatcherRule.testDispatcher) {
        repository.detailsResult = FakeBillingRepository.networkError()
        val viewModel = loadedViewModel()
        assertEquals(BillingDetailsUiState.Error, viewModel.uiState.value)

        val details = billingDetails()
        repository.detailsResult = Result.success(details)
        viewModel.load()
        advanceUntilIdle()

        assertEquals(BillingDetailsUiState.Success(details), viewModel.uiState.value)
    }

    @Test
    fun `successful delete emits Deleted`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = loadedViewModel()

        viewModel.delete()
        advanceUntilIdle()

        assertEquals(listOf(BILLING_ID), repository.deletedIds)
        assertEquals(BillingDetailsEvent.Deleted, viewModel.events.first())
    }

    @Test
    fun `failed delete emits DeleteFailed and re-enables the button`() = runTest(mainDispatcherRule.testDispatcher) {
        repository.deleteResult = FakeBillingRepository.networkError()
        val viewModel = loadedViewModel()

        viewModel.delete()
        assertTrue((viewModel.uiState.value as BillingDetailsUiState.Success).isDeleting)

        advanceUntilIdle()
        assertEquals(BillingDetailsEvent.DeleteFailed, viewModel.events.first())
        assertEquals(false, (viewModel.uiState.value as BillingDetailsUiState.Success).isDeleting)
    }

    @Test
    fun `double tap on delete sends a single request`() = runTest(mainDispatcherRule.testDispatcher) {
        val viewModel = loadedViewModel()

        viewModel.delete()
        viewModel.delete()
        advanceUntilIdle()

        assertEquals(listOf(BILLING_ID), repository.deletedIds)
    }

    @Test
    fun `delete is ignored until details are loaded`() = runTest(mainDispatcherRule.testDispatcher) {
        repository.detailsResult = FakeBillingRepository.networkError()
        val viewModel = loadedViewModel()

        viewModel.delete()
        advanceUntilIdle()

        assertTrue(repository.deletedIds.isEmpty())
    }
}
