package fr.outadoc.woolly.ui.feature.search

import androidx.compose.runtime.Composable
import fr.outadoc.woolly.common.feature.search.SearchSubScreen
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource

@Composable
fun SearchSubScreen.getTitle() = when (this) {
    SearchSubScreen.Statuses -> stringResource(MR.strings.search_statuses_title)
    SearchSubScreen.Accounts -> stringResource(MR.strings.search_accounts_title)
    SearchSubScreen.Hashtags -> stringResource(MR.strings.search_hashtags_title)
}
