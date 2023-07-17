package fr.outadoc.woolly.common.feature.status

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class NumberFormatTests {

    @Test
    fun testBelowZero() {
        assertFails {
            (-4L).formatShort(forceEnglishLocale = true)
        }
    }

    @Test
    fun testBelow100() {
        assertEquals("3", 3L.formatShort(forceEnglishLocale = true))
        assertEquals("42", 42L.formatShort(forceEnglishLocale = true))
        assertEquals("99", 99L.formatShort(forceEnglishLocale = true))
    }

    @Test
    fun testBelow1k() {
        assertEquals("100", 100L.formatShort(forceEnglishLocale = true))
        assertEquals("130", 130L.formatShort(forceEnglishLocale = true))
        assertEquals("999", 999L.formatShort(forceEnglishLocale = true))
    }

    @Test
    fun testBelow100k() {
        assertEquals("1k", 1000L.formatShort(forceEnglishLocale = true))
        assertEquals("1.5k", 1500L.formatShort(forceEnglishLocale = true))
        assertEquals("1.5k", 1504L.formatShort(forceEnglishLocale = true))
        assertEquals("1.5k", 1540L.formatShort(forceEnglishLocale = true))
        assertEquals("5k", 5034L.formatShort(forceEnglishLocale = true))
    }

    @Test
    fun testBelow1m() {
        assertEquals("100k", 100_000L.formatShort(forceEnglishLocale = true))
        assertEquals("100k", 100_670L.formatShort(forceEnglishLocale = true))
        assertEquals("512k", 512_131L.formatShort(forceEnglishLocale = true))
        assertEquals("999k", 999_999L.formatShort(forceEnglishLocale = true))
    }

    @Test
    fun testBelow99m() {
        assertEquals("1m", 1_000_000L.formatShort(forceEnglishLocale = true))
        assertEquals("4.3m", 4_300_000L.formatShort(forceEnglishLocale = true))
        assertEquals("54.2m", 54_200_000L.formatShort(forceEnglishLocale = true))
        assertEquals("99.6m", 99_600_000L.formatShort(forceEnglishLocale = true))
    }

    @Test
    fun testMax() {
        assertEquals("99m+", 120_000_000L.formatShort(forceEnglishLocale = true))
        assertEquals("99m+", Long.MAX_VALUE.formatShort(forceEnglishLocale = true))
    }
}