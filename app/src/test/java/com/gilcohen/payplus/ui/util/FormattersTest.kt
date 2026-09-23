package com.gilcohen.payplus.ui.util

import com.gilcohen.payplus.domain.model.Currency
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.TimeZone

class FormattersTest {

    private lateinit var originalTimeZone: TimeZone

    @Before
    fun setUp() {
        originalTimeZone = TimeZone.getDefault()
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    }

    @After
    fun tearDown() {
        TimeZone.setDefault(originalTimeZone)
    }

    @Test
    fun `formatDateTime uses dd-MM-yyyy HH mm`() {
        // 2022-08-21T12:00:00Z
        assertEquals("21-08-2022 12:00", formatDateTime(1_661_083_200_000))
    }

    @Test
    fun `formatDateTime pads single digit day, month and time`() {
        // 2020-01-02T03:04:00Z
        assertEquals("02-01-2020 03:04", formatDateTime(1_577_934_240_000))
    }

    @Test
    fun `formatPrice rounds to two decimals with thousands separators`() {
        assertEquals("25,357.53", formatPrice(25357.52885790945))
        assertEquals("14.40", formatPrice(14.4))
        assertEquals("0.00", formatPrice(0.0))
    }

    @Test
    fun `formatAmount prefixes the currency symbol`() {
        assertEquals("₪14.40", formatAmount(14.4, Currency.ILS))
        assertEquals("$1,000.00", formatAmount(1000.0, Currency.USD))
        assertEquals("14.40", formatAmount(14.4, Currency.UNKNOWN))
    }

    @Test
    fun `maskCardNumber keeps only the last four digits`() {
        assertEquals("****2345", maskCardNumber("4580123412342345"))
    }

    @Test
    fun `maskCardNumber leaves short numbers unchanged`() {
        assertEquals("1234", maskCardNumber("1234"))
        assertEquals("", maskCardNumber(""))
    }

    @Test
    fun `formatEntryNumber shows entry out of total`() {
        assertEquals("3/6", formatEntryNumber(entryNumber = 3, totalEntryCount = 6))
    }

    @Test
    fun `ltr wraps text in a left-to-right isolate`() {
        assertEquals("⁦3/6⁩", ltr("3/6"))
    }
}
