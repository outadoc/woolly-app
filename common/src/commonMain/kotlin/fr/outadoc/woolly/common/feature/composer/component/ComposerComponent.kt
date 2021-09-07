package fr.outadoc.woolly.common.feature.composer.component

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.lifecycle.doOnDestroy
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.request.StatusCreate
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.composer.StatusPoster
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ComposerComponent(
    componentContext: ComponentContext,
    private val statusPoster: StatusPoster,
    private val clientProvider: MastodonClientProvider
) : ComponentContext by componentContext {

    private val componentScope = MainScope()

    data class State(
        val message: String = "",
        val inReplyToStatus: Status? = null,
        val isLoading: Boolean = false
    )

    private val _state = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun onMessageChange(message: String) {
        _state.value = _state.value.copy(message = message)
    }

    fun onSubmit() {
        val currentState = _state.value
        _state.value = State()
        statusPoster.enqueueStatus(
            StatusCreate(
                status = currentState.message,
                inReplyToId = currentState.inReplyToStatus?.statusId
            )
        )
    }

    fun loadStatusRepliedTo(acct: String, statusId: String) {
        componentScope.launch {
            _state.value = _state.value.copy(
                message = "@$acct ",
                isLoading = true
            )

            val status = clientProvider.mastodonClient.value
                ?.statuses?.getStatus(statusId)

            _state.value = _state.value.copy(
                inReplyToStatus = status,
                isLoading = false
            )
        }
    }

    init {
        lifecycle.doOnDestroy {
            componentScope.cancel()
        }
    }
}