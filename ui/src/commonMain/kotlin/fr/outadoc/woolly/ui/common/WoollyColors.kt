package fr.outadoc.woolly.ui.common

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object WoollyTheme {
    val BoostColor = Color(0xff2b90d9)
    val FavouriteColor = Color(0xffca8f04)
    val AvatarShape = CircleShape
}

@Composable
expect fun woollyLightColors(): ColorScheme

@Composable
expect fun woollyDarkColors(): ColorScheme
