package fr.outadoc.woolly.ui.navigation.main

import androidx.compose.runtime.Composable

abstract class Content {
    abstract val main: @Composable () -> Unit
    abstract val topBar: @Composable () -> Unit
    abstract val bottomBar: @Composable () -> Unit
    abstract val narrowDrawerContent: @Composable () -> Unit
    abstract val wideDrawerContent: @Composable () -> Unit
    abstract val floatingActionButton: @Composable () -> Unit
}

fun <T : Any> T.asContent(
    topBar: @Composable (T) -> Unit,
    bottomBar: @Composable (T) -> Unit,
    narrowDrawerContent: @Composable (T) -> Unit,
    wideDrawerContent: @Composable (T) -> Unit,
    floatingActionButton: @Composable (T) -> Unit,
    main: @Composable (T) -> Unit,
): Content = object : Content() {

    override val main: () -> Unit =
        @Composable { main(this@asContent) }

    override val topBar: () -> Unit =
        @Composable { topBar(this@asContent) }

    override val bottomBar: () -> Unit =
        @Composable { bottomBar(this@asContent) }

    override val narrowDrawerContent: () -> Unit =
        @Composable { narrowDrawerContent(this@asContent) }

    override val wideDrawerContent: () -> Unit =
        @Composable { wideDrawerContent(this@asContent) }

    override val floatingActionButton: () -> Unit =
        @Composable { floatingActionButton(this@asContent) }
}
