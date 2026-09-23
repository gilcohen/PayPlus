package com.gilcohen.payplus.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gilcohen.payplus.ui.details.BillingDetailsRoute
import com.gilcohen.payplus.ui.list.BillingListRoute

@Composable
fun PayPlusNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ListDestination) {
        composable<ListDestination> {
            BillingListRoute(
                onItemClick = { billingId -> navController.navigate(DetailsDestination(billingId)) },
            )
        }
        composable<DetailsDestination> {
            BillingDetailsRoute()
        }
    }
}
