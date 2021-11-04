package fr.outadoc.woolly.common.feature.composer.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.mastodonk.api.entity.request.StatusCreate
import fr.outadoc.woolly.common.feature.account.AccountRepository
import fr.outadoc.woolly.common.feature.client.MastodonClientProvider
import fr.outadoc.woolly.common.feature.composer.StatusPoster
import fr.outadoc.woolly.common.getScope
import kotlinx.coroutines.flow.*

class ComposerComponent(
    componentContext: ComponentContext,
    private val statusPoster: StatusPoster,
    private val clientProvider: MastodonClientProvider,
    private val accountRepository: AccountRepository
) : ComponentContext by componentContext {

    private val componentScope = getScope()

    data class State(
        val message: String = "",
        val inReplyToStatus: Status? = null,
        val currentAccount: Account? = null,
        val isLoading: Boolean = false,
    )

    private val _repliedToIdFlow = MutableStateFlow<String?>(null)
    private val _messageFlow = MutableStateFlow("")

    val state = flow {
        var currentState = State()

        combine(
            _repliedToIdFlow,
            _messageFlow,
            accountRepository.currentAccount,
            clientProvider.mastodonClient
        ) { repliedToId, message, currentAccount, client ->
            if (currentState.inReplyToStatus?.statusId != repliedToId) {
                currentState = currentState.copy(
                    isLoading = true
                )

                emit(currentState)

                val status = try {
                    repliedToId?.let { statusId ->
                        client?.statuses?.getStatus(statusId)
                    }
                } catch (t: Throwable) {
                    null
                }

                currentState = currentState.copy(
                    inReplyToStatus = status,
                    isLoading = false
                )
            }

            currentState = currentState.copy(
                currentAccount = currentAccount,
                message = message
            )

            emit(currentState)

        }.collect()

    }.stateIn(
        componentScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = State()
    )

    fun onMessageChange(message: String) {
        _messageFlow.tryEmit(message)
    }

    fun onSubmit() {
        val currentState = state.value
        statusPoster.enqueueStatus(
            StatusCreate(
                status = currentState.message,
                inReplyToId = currentState.inReplyToStatus?.statusId
            )
        )
    }

    fun loadStatusRepliedTo(statusId: String) {
        _repliedToIdFlow.tryEmit(statusId)
    }
}