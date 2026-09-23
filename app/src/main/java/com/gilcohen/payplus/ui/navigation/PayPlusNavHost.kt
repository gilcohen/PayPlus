package com.gilcohen.payplus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gilcohen.payplus.ui.details.BillingDetailsRoute
import com.gilcohen.payplus.ui.list.BillingListRoute

/** Result key the details screen sets on the list entry after a successful delete. */
private const val ENTRY_DELETED_KEY = "entry_deleted"

@Composable
fun PayPlusNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ListDestination) {
        composable<ListDestination> { backStackEntry ->
            val savedStateHandle = backStackEntry.savedStateHandle
            val entryDeleted by savedStateHandle
                .getStateFlow(ENTRY_DELETED_KEY, false)
                .collectAsStateWithLifecycle()

            BillingListRoute(
                onItemClick = { billingId -> navController.navigate(DetailsDestination(billingId)) },
                refreshRequested = entryDeleted,
                onRefreshHandled = { savedStateHandle[ENTRY_DELETED_KEY] = false },
            )
        }
        composable<DetailsDestination> {
            BillingDetailsRoute(
                onDeleted = {
                    navController.previousBackStackEntry?.savedStateHandle?.set(ENTRY_DELETED_KEY, true)
                    navController.popBackStack()
                },
            )
        }
    }
}
