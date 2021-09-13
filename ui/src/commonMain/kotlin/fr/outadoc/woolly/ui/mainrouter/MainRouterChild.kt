package fr.outadoc.woolly.ui.mainrouter

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.feature.mainrouter.component.MainContent
import fr.outadoc.woolly.ui.feature.account.AccountDetailsScreen
import fr.outadoc.woolly.ui.feature.account.MyAccountScreen
import fr.outadoc.woolly.ui.feature.bookmarks.BookmarksScreen
import fr.outadoc.woolly.ui.feature.composer.ComposerScreen
import fr.outadoc.woolly.ui.feature.favourites.FavouritesScreen
import fr.outadoc.woolly.ui.feature.home.HomeTimelineScreen
import fr.outadoc.woolly.ui.feature.media.ImageViewerScreen
import fr.outadoc.woolly.ui.feature.notifications.NotificationsScreen
import fr.outadoc.woolly.ui.feature.publictimeline.PublicTimelineScreen
import fr.outadoc.woolly.ui.feature.search.SearchScreen
import fr.outadoc.woolly.ui.feature.statusdetails.StatusDetailsScreen

@Composable
fun MainRouterChild(
    content: MainContent,
    insets: PaddingValues = PaddingValues(),
    onStatusClick: (Status) -> Unit = {},
    onAttachmentClick: (Attachment) -> Unit = {},
    onStatusReplyClick: (Status) -> Unit = {},
    onAccountClick: (Account) -> Unit = {},
    onComposerDismissed: () -> Unit = {}
) {
    when (content) {
        is MainContent.HomeTimeline -> HomeTimelineScreen(
            component = content.component,
            insets = insets,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onStatusReplyClick = onStatusReplyClick,
            onAccountClick = onAccountClick
        )
        is MainContent.PublicTimeline -> PublicTimelineScreen(
            component = content.component,
            insets = insets,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onStatusReplyClick = onStatusReplyClick,
            onAccountClick = onAccountClick
        )
        is MainContent.Notifications -> NotificationsScreen(
            component = content.component,
            insets = insets,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onAccountClick = onAccountClick
        )
        is MainContent.Search -> SearchScreen(
            component = content.component,
            insets = insets,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onStatusReplyClick = onStatusReplyClick,
            onAccountClick = onAccountClick
        )
        is MainContent.MyAccount -> MyAccountScreen(
            component = content.component,
            insets = insets
        )
        is MainContent.Bookmarks -> BookmarksScreen(
            component = content.component,
            insets = insets,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onStatusReplyClick = onStatusReplyClick,
            onAccountClick = onAccountClick
        )
        is MainContent.Favourites -> FavouritesScreen(
            component = content.component,
            insets = insets,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onStatusReplyClick = onStatusReplyClick,
            onAccountClick = onAccountClick
        )
        is MainContent.StatusDetails -> StatusDetailsScreen(
            component = content.component,
            insets = insets,
            statusId = content.configuration.statusId,
            onStatusClick = onStatusClick,
            onAttachmentClick = onAttachmentClick,
            onStatusReplyClick = onStatusReplyClick,
            onAccountClick = onAccountClick
        )
        is MainContent.ImageViewer -> ImageViewerScreen(
            component = content.component,
            image = content.configuration.image
        )
        is MainContent.StatusComposer -> ComposerScreen(
            component = content.component,
            inReplyToStatusPayload = content.configuration.inReplyToStatusPayload,
            onDismiss = onComposerDismissed
        )
        is MainContent.AccountDetails -> AccountDetailsScreen(
            component = content.component,
            accountId = content.configuration.accountId
        )
    }
}
