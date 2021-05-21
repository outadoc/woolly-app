package fr.outadoc.woolly.common.ui

import androidx.compose.material.AppBarDefaults
import androidx.compose.material.LocalElevationOverlay
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun WoollyTheme(
    isDarkMode: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = if (isDarkMode) darkColors() else lightColors(),
    ) {
        val systemUiController = rememberSystemUiController()

        val baseColor =
            if (isDarkMode) MaterialTheme.colors.surface
            else MaterialTheme.colors.primaryVariant

        val topColor = baseColor.elevated(AppBarDefaults.TopAppBarElevation)
        val bottomColor = baseColor.elevated(AppBarDefaults.BottomAppBarElevation)

        SideEffect {
            systemUiController.setStatusBarColor(topColor)
            systemUiController.setNavigationBarColor(bottomColor)
        }

        content()
    }
}

@Composable
private fun Color.elevated(elevation: Dp): Color {
    val elevationOverlay = LocalElevationOverlay.current
    return if (this == MaterialTheme.colors.surface && elevationOverlay != null) {
        elevationOverlay.apply(this, elevation)
    } else {
        this
    }
}

object WoollyTheme {
    val BoostColor = Color(0xff2b90d9)
    val FavouriteColor = Color(0xffca8f04)
}
