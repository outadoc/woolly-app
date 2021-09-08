package fr.outadoc.woolly.common

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.lifecycle.doOnDestroy
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

fun ComponentContext.getScope() =
    MainScope().also { scope ->
        lifecycle.doOnDestroy {
            scope.cancel()
        }
    }
