package com.gilcohen.payplus.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
data object ListDestination

@Serializable
data class DetailsDestination(val billingId: Long)
