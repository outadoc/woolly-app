package fr.outadoc.woolly.common.feature.composer.component

import com.arkivanov.decompose.ComponentContext
import fr.outadoc.mastodonk.api.entity.request.StatusCreate
import fr.outadoc.woolly.common.feature.composer.StatusPoster
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ComposerComponent(
    componentContext: ComponentContext,
    private val statusPoster: StatusPoster
) : ComponentContext by componentContext {

    sealed class State {
        data class Composing(val message: String = "") : State()
    }

    private val _state = MutableStateFlow<State>(State.Composing())
    val state = _state.asStateFlow()

    fun onMessageChange(message: String) {
        if (_state.value !is State.Composing) return
        _state.value = State.Composing(
            message = message
        )
    }

    fun onSubmit() {
        val currentState = (_state.value as? State.Composing) ?: return
        _state.value = State.Composing()
        statusPoster.enqueueStatus(
            StatusCreate(
                status = currentState.message
            )
        )
    }
}