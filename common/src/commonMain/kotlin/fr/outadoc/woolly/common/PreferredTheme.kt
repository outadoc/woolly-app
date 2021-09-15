package fr.outadoc.woolly.common

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class PreferredTheme {

    @SerialName("light")
    Light,

    @SerialName("dark")
    Dark,

    @SerialName("system")
    FollowSystem
}
