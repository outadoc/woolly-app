package fr.outadoc.woolly.common.feature.search.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalUriHandler
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.woolly.common.feature.search.viewmodel.TrendingViewModel
import fr.outadoc.woolly.common.ui.WoollyListItem
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun TrendingScreen(insets: PaddingValues) {
    val di = LocalDI.current
    val vm by di.instance<TrendingViewModel>()
    val trends by vm.trendingTags.collectAsState(initial = emptyList())

    val uriHandler = LocalUriHandler.current

    LazyColumn(contentPadding = insets) {
        items(trends) { tag ->
            TrendingTag(
                tag = tag,
                onClick = { uriHandler.openUri(tag.url) }
            )
        }
    }
}

@Composable
fun TrendingTag(tag: Tag, onClick: () -> Unit) {
    WoollyListItem(
        icon = {
            Icon(
                Icons.Default.TrendingUp,
                contentDescription = "Trending"
            )
        },
        title = {
            Text(
                text = tag.name,
                style = MaterialTheme.typography.body1
            )
        },
        onClick = onClick
    )
}
