package fr.outadoc.woolly.ui.feature.composer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Reply
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.feature.status.StatusOrBoost
import fr.outadoc.woolly.ui.strings.stringResource

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun ComposerAndParentStatus(
    modifier: Modifier = Modifier,
    message: String,
    account: Account? = null,
    isLoadingParentStatus: Boolean,
    inReplyToStatus: Status?,
    onMessageChange: (String) -> Unit,
    onSubmit: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        state = rememberLazyListState(
            // Skip first item (the header)
            initialFirstVisibleItemIndex = 1
        )
    ) {
        item {
            when {
                isLoadingParentStatus -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    }
                }
                inReplyToStatus != null -> {
                    StatusOrBoost(
                        modifier = Modifier.padding(bottom = 16.dp),
                        status = inReplyToStatus
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.fillParentMaxHeight()) {
                AnimatedVisibility(
                    modifier = Modifier.padding(bottom = 8.dp),
                    visible = inReplyToStatus != null
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Reply,
                            contentDescription = stringResource(MR.strings.statusComposer_replyingTo_cd)
                        )
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(
                                MR.strings.statusComposer_replyingTo_message,
                                inReplyToStatus?.account?.displayNameOrAcct ?: ""
                            ),
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }

                ComposerBox(
                    modifier = Modifier.padding(top = 8.dp),
                    message = message,
                    account = account,
                    onMessageChange = onMessageChange,
                    onSubmit = onSubmit
                )
            }
        }
    }
}
