package fr.outadoc.woolly.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.*

@Composable
actual fun stringResource(resource: StringResource): String =
    StringDesc.Resource(resource).toString(LocalContext.current)

@Composable
actual fun stringResource(resource: StringResource, vararg args: Any): String =
    StringDesc.ResourceFormatted(resource, *args).toString(LocalContext.current)

@Composable
actual fun stringResource(resource: PluralsResource, quantity: Int): String =
    StringDesc.Plural(resource, quantity).toString(LocalContext.current)

@Composable
actual fun stringResource(resource: PluralsResource, quantity: Int, vararg args: Any): String =
    StringDesc.PluralFormatted(resource, quantity, *args).toString(LocalContext.current)
