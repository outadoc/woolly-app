package fr.outadoc.woolly.common.ui

enum class ColorScheme(val value: String) {
    Light("light"), Dark("dark");

    companion object {
        fun from(value: String): ColorScheme =
            values().first { it.value == value }
    }
}
