package fr.outadoc.woolly.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ColorScheme {

    @SerialName("light")
    Light,

    @SerialName("dark")
    Dark
}
