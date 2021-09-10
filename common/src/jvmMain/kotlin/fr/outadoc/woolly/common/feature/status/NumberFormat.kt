package fr.outadoc.woolly.common.feature.status

import java.util.*

actual fun Long.formatShort(forceEnglishLocale: Boolean): String {
    val locale =
        if (forceEnglishLocale) Locale.ENGLISH
        else Locale.getDefault(Locale.Category.FORMAT)

    return when {
        this < 0 -> throw IllegalArgumentException()
        this < 1_000 -> this.toString()
        this < 100_000 -> "%.1f".format(locale, this / 1_000f).formatTrimZeroes() + "k"
        this < 1_000_000 -> "%d".format(this / 1_000L) + "k"
        this < 100_000_000 -> "%.1f".format(locale, this / 1_000_000f).formatTrimZeroes() + "m"
        else -> "99m+"
    }
}

private fun String.formatTrimZeroes() =
    if (length > 1) trimEnd('0').trimEnd { c -> !c.isDigit() }
    else this
