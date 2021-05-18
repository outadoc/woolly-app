package fr.outadoc.woolly.common.feature.search.repository

import androidx.paging.PagingSource
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.Tag
import fr.outadoc.mastodonk.api.entity.paging.PageInfo
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchAccountsSource
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchHashtagsSource
import fr.outadoc.mastodonk.paging.api.endpoint.search.searchStatusesSource
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.client.latestClientOrThrow

class SearchRepository(private val clientProvider: MastodonClientProvider) {

    private val client get() = clientProvider.latestClientOrThrow

    fun getStatusSearchResultsSource(term: String): PagingSource<PageInfo, Status> {
        return client.search.searchStatusesSource(q = term)
    }

    fun getAccountSearchResultsSource(term: String): PagingSource<PageInfo, Account> {
        return client.search.searchAccountsSource(q = term)
    }

    fun getHashtagSearchResultsSource(term: String): PagingSource<PageInfo, Tag> {
        return client.search.searchHashtagsSource(q = term)
    }
}
