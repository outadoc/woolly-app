package fr.outadoc.woolly.ui.feature.notifications

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.notifications.viewmodel.NotificationsViewModel
import org.kodein.di.compose.LocalDI
import org.kodein.di.instance

@Composable
fun NotificationsScreen(
    insets: PaddingValues,
    listState: LazyListState,
    onStatusClick: (Status) -> Unit = {}
) {
    val di = LocalDI.current
    val vm by di.instance<NotificationsViewModel>()

    NotificationList(
        insets = insets,
        notificationFlow = vm.pagingData,
        lazyListState = listState,
        onStatusClick = onStatusClick
    )
}
