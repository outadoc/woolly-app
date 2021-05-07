package fr.outadoc.mastodonk.common

import fr.outadoc.mastodonk.api.entity.paging.Page

sealed class PageState<T> {
    class Loading<T> : PageState<T>()
    data class Content<T>(val page: Page<T>) : PageState<T>()
}
