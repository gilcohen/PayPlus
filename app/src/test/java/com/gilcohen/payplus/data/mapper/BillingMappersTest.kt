package com.gilcohen.payplus.data.mapper

import com.gilcohen.payplus.data.remote.dto.BillingEntryDetailsDto
import com.gilcohen.payplus.data.remote.dto.BillingEntryHeaderDto
import com.gilcohen.payplus.domain.model.BillingSource
import com.gilcohen.payplus.domain.model.BillingStatus
import com.gilcohen.payplus.domain.model.Currency
import com.gilcohen.payplus.testutil.billingDetails
import com.gilcohen.payplus.testutil.billingHeader
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BillingMappersTest {

    private val headerDto = BillingEntryHeaderDto(
        id = 5165,
        price = 25357.52885790945,
        created = 1592293845,
        entryNumber = 2,
        totalEntryCount = 17,
        source = "Terminal",
        currencyCode = "USD",
        cardType = "Amex",
    )

    private val detailsDto = BillingEntryDetailsDto(
        id = 5165,
        price = 25357.52885790945,
        created = 1592293845,
        entryNumber = 2,
        totalEntryCount = 17,
        currencyCode = "ILS",
        amountPaid = 2992.4083006040337,
        status = "Rejected",
        cardNumber = "7359327724873",
        cardType = "Amex",
        issuer = "Jcb",
        source = "Pos",
        terminalName = "Emv 0",
        approvalNumber = "9748301211203",
        voucherNumber = "77-201-69",
    )

    @Test
    fun `header maps all fields and converts created from seconds to millis`() {
        val header = headerDto.toDomain()

        assertEquals(5165L, header.id)
        assertEquals(25357.52885790945, header.price, 0.0)
        assertEquals(1_592_293_845_000L, header.createdAtMillis)
        assertEquals(2, header.entryNumber)
        assertEquals(17, header.totalEntryCount)
        assertEquals(BillingSource.TERMINAL, header.source)
        assertEquals(Currency.USD, header.currency)
        assertEquals("Amex", header.cardType)
    }

    @Test
    fun `details maps all fields and parses enums`() {
        val details = detailsDto.toDomain()

        assertEquals(1_592_293_845_000L, details.createdAtMillis)
        assertEquals(Currency.ILS, details.currency)
        assertEquals(BillingStatus.REJECTED, details.status)
        assertEquals(BillingSource.POS, details.source)
        assertEquals(2992.4083006040337, details.amountPaid, 0.0)
        assertEquals("7359327724873", details.cardNumber)
        assertEquals("Jcb", details.issuer)
        assertEquals("Emv 0", details.terminalName)
        assertEquals("9748301211203", details.approvalNumber)
        assertEquals("77-201-69", details.voucherNumber)
    }

    @Test
    fun `enum parsing ignores case`() {
        assertEquals(BillingSource.MANUAL, BillingSource.fromApi("manual"))
        assertEquals(Currency.ILS, Currency.fromApi("ils"))
        assertEquals(BillingStatus.PASSED, BillingStatus.fromApi("PASSED"))
    }

    @Test
    fun `unexpected values map to UNKNOWN`() {
        assertEquals(BillingSource.UNKNOWN, BillingSource.fromApi("Kiosk"))
        assertEquals(Currency.UNKNOWN, Currency.fromApi("EUR"))
        assertEquals(BillingStatus.UNKNOWN, BillingStatus.fromApi("Pending"))
        assertEquals(BillingStatus.UNKNOWN, BillingStatus.fromApi("Unknown"))
    }

    @Test
    fun `isMasterCard matches MasterCard only, ignoring case`() {
        assertTrue(billingHeader(cardType = "MasterCard").isMasterCard)
        assertTrue(billingHeader(cardType = "mastercard").isMasterCard)
        assertFalse(billingHeader(cardType = "Visa").isMasterCard)
        assertFalse(billingHeader(cardType = "Meastro").isMasterCard)
    }

    @Test
    fun `remainingAmount is price minus amount paid`() {
        assertEquals(60.0, billingDetails(price = 100.0, amountPaid = 40.0).remainingAmount, 0.0001)
    }

    @Test
    fun `remainingAmount never goes below zero`() {
        assertEquals(0.0, billingDetails(price = 100.0, amountPaid = 150.0).remainingAmount, 0.0)
    }
}
