package fr.outadoc.woolly.common.feature.timeline.usecase

import fr.outadoc.mastodonk.client.MastodonClient
import fr.outadoc.woolly.common.feature.timeline.AnnotatedStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GetStatusListUseCase(
    private val mastodonClient: MastodonClient,
    private val annotateStatusUseCase: AnnotateStatusUseCase
) {
    suspend operator fun invoke(onlyLocal: Boolean? = null): List<AnnotatedStatus> {
        return withContext(Dispatchers.IO) {
            val res = mastodonClient.timelines.getPublicTimeline(onlyLocal = onlyLocal)
            withContext(Dispatchers.Default) {
                res.contents.map { status ->
                    annotateStatusUseCase(status)
                }
            }
        }
    }
}