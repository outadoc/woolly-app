package fr.outadoc.woolly.ui.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import fr.outadoc.woolly.ui.feature.error.ErrorScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LazyItemScope.ListExtremityState(
    state: LoadState, onRetry: () -> Unit,
    animationDirection: Alignment.Vertical
) {
    AnimatedVisibility(
        visible = state !is LoadState.NotLoading,
        enter = expandVertically(expandFrom = animationDirection),
        exit = shrinkVertically(shrinkTowards = animationDirection)
    ) {
        Column(
            modifier = Modifier
                .fillParentMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (state) {
                is LoadState.Loading -> CircularProgressIndicator()

                is LoadState.Error -> ErrorScreen(
                    modifier = Modifier.fillMaxWidth(),
                    error = state.error,
                    onRetry = onRetry
                )

                else -> {
                }
            }
        }
    }
}
