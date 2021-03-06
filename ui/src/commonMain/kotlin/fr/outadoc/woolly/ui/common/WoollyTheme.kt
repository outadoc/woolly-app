package fr.outadoc.woolly.ui.common

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun WoollyTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDarkMode) woollyDarkColors() else woollyLightColors(),
        content = content,
    )
}

fun woollyLightColors() = lightColors(
    primary = Color(0xff2196f3),
    primaryVariant = Color(0xff0069c0),
    onPrimary = Color(0xffffffff),
    secondary = Color(0xff9ccc65),
    secondaryVariant = Color(0xff6b9b37),
    onSecondary = Color(0xff000000)
)

fun woollyDarkColors() = darkColors(
    primary = Color(0xff2196f3),
    primaryVariant = Color(0xff0069c0),
    onPrimary = Color(0xffffffff),
    secondary = Color(0xff9ccc65),
    secondaryVariant = Color(0xff6b9b37),
    onSecondary = Color(0xff000000)
)

object WoollyTheme {
    val BoostColor = Color(0xff2b90d9)
    val FavouriteColor = Color(0xffca8f04)
    val AvatarShape = CircleShape
}
