package fr.outadoc.mastodonk.common

import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.paging.Page

sealed class ListState {
    object Loading : ListState()
    data class Content(val page: Page<List<Status>>) : ListState()
}
