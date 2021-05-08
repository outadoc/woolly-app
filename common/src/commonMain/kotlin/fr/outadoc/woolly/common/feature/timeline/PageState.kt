package fr.outadoc.woolly.common.feature.timeline

sealed class PageState<T> {
    class Loading<T> : PageState<T>()
    data class Content<T>(val page: T) : PageState<T>()
}
