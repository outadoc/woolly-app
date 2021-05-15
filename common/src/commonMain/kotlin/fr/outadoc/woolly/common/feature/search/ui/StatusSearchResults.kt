package fr.outadoc.woolly.common.feature.search.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.search.repository.SearchRepository
import fr.outadoc.woolly.common.ui.Timeline
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun StatusSearchResults(term: String, insets: PaddingValues) {
    val di = LocalDI.current
    val repo by di.instance<SearchRepository>()

    Timeline(
        insets = insets,
        pagingSourceFactory = {
            repo.getStatusSearchResultsSource(term)
        }
    )
}