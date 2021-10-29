package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Context
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.status.StatusAction
import fr.outadoc.woolly.ui.feature.status.Status

@Composable
fun StatusWithContext(
    modifier: Modifier = Modifier,
    status: Status,
    context: Context,
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusAction: (StatusAction) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {}
) {
    // TODO move listState to component
    LazyColumn(
        modifier = modifier,
        state = rememberLazyListState(
            initialFirstVisibleItemIndex = context.ancestors.size
        )
    ) {
        if (context.ancestors.isNotEmpty()) {
            items(context.ancestors, key = { it.statusId }) { status ->
                Status(
                    modifier = Modifier
                        .clickable { onStatusClick(status) }
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        ),
                    status = status,
                    onStatusAction = onStatusAction,
                    onAttachmentClick = onAttachmentClick,
                    onStatusReplyClick = onStatusReplyClick,
                    onAccountClick = onAccountClick
                )

                Divider(thickness = Dp.Hairline)
            }
        }

        item(key = status.statusId) {
            StatusDetails(
                modifier = Modifier.padding(16.dp),
                statusOrBoost = status,
                onAttachmentClick = onAttachmentClick,
                onStatusAction = onStatusAction,
                onStatusReplyClick = onStatusReplyClick,
                onAccountClick = onAccountClick
            )
        }

        if (context.descendants.isNotEmpty()) {
            items(
                context.descendants,
                key = { it.statusId }
            ) { status ->
                Divider(thickness = Dp.Hairline)

                Status(
                    modifier = Modifier
                        .clickable { onStatusClick(status) }
                        .padding(
                            top = 16.dp,
                            start = 16.dp,
                            end = 16.dp,
                            bottom = 8.dp
                        ),
                    status = status,
                    onStatusAction = onStatusAction,
                    onAttachmentClick = onAttachmentClick,
                    onStatusReplyClick = onStatusReplyClick,
                    onAccountClick = onAccountClick
                )
            }
        }
    }
}
