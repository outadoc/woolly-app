package fr.outadoc.woolly.ui.feature.tags

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.mastodonk.api.entity.Tag
import kotlinx.coroutines.flow.Flow

@Composable
fun TrendingScreen(
    trendingTags: Flow<List<Tag>>,
    insets: PaddingValues = PaddingValues(),
    onHashtagClick: (String) -> Unit = {}
) {
    val trends by trendingTags.collectAsState(initial = emptyList())

    LazyColumn(contentPadding = insets) {
        items(trends) { tag ->
            TrendingTagListItem(
                tag = tag,
                onClick = { onHashtagClick(tag.name) }
            )
        }
    }
}
