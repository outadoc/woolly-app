package fr.outadoc.woolly.ui.feature.composer

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import fr.outadoc.woolly.common.feature.composer.viewmodel.ComposerViewModel
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun ComposerScreen(
    onDismiss: () -> Unit
) {
    val di = LocalDI.current
    val vm by di.instance<ComposerViewModel>()

    val state by vm.state.collectAsState()

    when (val state = state) {
        is ComposerViewModel.State.Composing -> {
            Column {
                // TODO nicer UI
                OutlinedTextField(
                    value = state.message,
                    onValueChange = { message ->
                        vm.onMessageChange(message)
                    }
                )

                Button(onClick = {
                    vm.onSubmit()
                    onDismiss()
                }) {
                    Text("Send")
                }
            }
        }
    }
}
