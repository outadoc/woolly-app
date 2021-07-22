package fr.outadoc.woolly.common

sealed class LoadState<T> {
    class Loading<T> : LoadState<T>()
    data class Loaded<T>(val value: T): LoadState<T>()
}
