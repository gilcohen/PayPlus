package com.gilcohen.payplus.domain.model

enum class BillingSource {
    TERMINAL, POS, MANUAL, UNKNOWN;

    companion object {
        fun fromApi(value: String): BillingSource =
            entries.firstOrNull { it != UNKNOWN && it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
    }
}

enum class Currency(val symbol: String) {
    USD("$"), ILS("₪"), UNKNOWN("");

    companion object {
        fun fromApi(value: String): Currency =
            entries.firstOrNull { it != UNKNOWN && it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
    }
}

enum class BillingStatus {
    PASSED, REJECTED, UNKNOWN;

    companion object {
        fun fromApi(value: String): BillingStatus =
            entries.firstOrNull { it != UNKNOWN && it.name.equals(value, ignoreCase = true) } ?: UNKNOWN
    }
}
