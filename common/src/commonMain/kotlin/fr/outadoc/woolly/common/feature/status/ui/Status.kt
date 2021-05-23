package fr.outadoc.woolly.common.feature.status.ui

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Gif
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Reply
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import com.wolt.blurhashkt.BlurHashDecoder
import fr.outadoc.mastodonk.api.entity.Account
import fr.outadoc.mastodonk.api.entity.Attachment
import fr.outadoc.mastodonk.api.entity.AttachmentType
import fr.outadoc.mastodonk.api.entity.Status
import fr.outadoc.woolly.common.displayNameOrAcct
import fr.outadoc.woolly.common.ui.StatusAction
import fr.outadoc.woolly.common.ui.WoollyTheme
import fr.outadoc.woolly.htmltext.HtmlText
import io.kamel.image.KamelImage
import io.kamel.image.lazyImageResource
import kotlinx.coroutines.Dispatchers
import kotlinx.datetime.Instant

@Composable
fun StatusPlaceholder() {
    Spacer(modifier = Modifier.height(64.dp))
}

@Composable
fun StatusOrBoost(
    modifier: Modifier = Modifier,
    status: Status,
    currentTime: Instant,
    onStatusAction: (StatusAction) -> Unit
) {
    val original = status.boostedStatus ?: status
    val boostedBy = if (status.boostedStatus != null) status.account else null
    StatusBodyWithPicture(
        modifier = modifier,
        status = original,
        boostedBy = boostedBy,
        currentTime = currentTime,
        onStatusAction = onStatusAction
    )
}

@Composable
fun StatusBodyWithPicture(
    modifier: Modifier = Modifier,
    status: Status,
    boostedBy: Account?,
    currentTime: Instant,
    onStatusAction: (StatusAction) -> Unit
) {
    Row(
        modifier = modifier.padding(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        )
    ) {
        ProfilePicture(
            modifier = Modifier.padding(end = 16.dp),
            account = status.account
        )

        StatusBodyWithActions(
            status = status,
            boostedBy = boostedBy,
            currentTime = currentTime,
            onStatusAction = onStatusAction
        )
    }
}

@Composable
fun StatusBodyWithActions(
    modifier: Modifier = Modifier,
    status: Status,
    boostedBy: Account?,
    currentTime: Instant,
    onStatusAction: (StatusAction) -> Unit
) {
    Column(modifier = modifier) {
        StatusBody(
            status = status,
            currentTime = currentTime
        )

        if (status.mediaAttachments.isNotEmpty()) {
            StatusMediaList(
                modifier = Modifier.padding(
                    top = 16.dp,
                    bottom = 8.dp
                ),
                media = status.mediaAttachments,
                isSensitive = status.isSensitive
            )
        }

        StatusFooter(
            modifier = Modifier.padding(top = 8.dp),
            boostedBy = boostedBy
        )

        StatusActions(
            modifier = Modifier.offset(x = (-16).dp),
            status = status,
            onStatusAction = onStatusAction
        )
    }
}

@Composable
fun StatusBody(
    modifier: Modifier = Modifier,
    status: Status,
    currentTime: Instant
) {
    Column(modifier = modifier.fillMaxWidth()) {
        StatusHeader(
            modifier = Modifier.padding(bottom = 8.dp),
            status = status,
            currentTime = currentTime
        )

        SelectionContainer {
            HtmlText(
                html = status.content,
                style = MaterialTheme.typography.body2
            )
        }
    }
}

@Composable
fun StatusFooter(modifier: Modifier = Modifier, boostedBy: Account?) {
    Column(modifier = modifier) {
        boostedBy?.let { boostedBy ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(end = 8.dp),
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Boosted",
                    tint = LocalContentColor.current.copy(alpha = 0.7f)
                )

                Text(
                    "${boostedBy.displayNameOrAcct} boosted",
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    color = LocalContentColor.current.copy(alpha = 0.7f),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun StatusHeader(
    modifier: Modifier = Modifier,
    status: Status,
    currentTime: Instant
) {
    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                modifier = Modifier
                    .alignByBaseline()
                    .fillMaxWidth(0.8f),
                text = status.account.displayNameOrAcct,
                style = MaterialTheme.typography.subtitle1,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            RelativeTime(
                modifier = Modifier.alignByBaseline(),
                currentTime = currentTime,
                time = status.createdAt,
                style = MaterialTheme.typography.subtitle2,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                maxLines = 1
            )
        }

        if (status.account.displayName.isNotBlank()) {
            Text(
                text = "@${status.account.acct}",
                style = MaterialTheme.typography.subtitle2,
                color = LocalContentColor.current.copy(alpha = 0.7f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun StatusActions(
    modifier: Modifier = Modifier,
    status: Status,
    onStatusAction: (StatusAction) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.requiredWidth(min(maxWidth, 250.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            StatusAction(
                icon = Icons.Default.Reply,
                checked = false,
                contentDescription = "Reply",
                counter = status.repliesCount,
                onCheckedChange = {}
            )

            StatusAction(
                modifier = Modifier.padding(start = 16.dp),
                checked = status.isBoosted == true,
                checkedColor = WoollyTheme.BoostColor,
                icon = Icons.Default.Repeat,
                contentDescription = if (status.isBoosted == true) "Undo boost" else "Boost",
                counter = status.boostsCount,
                onCheckedChange = { boosted ->
                    onStatusAction(
                        if (boosted) StatusAction.Boost(status)
                        else StatusAction.UndoBoost(status)
                    )
                }
            )

            StatusAction(
                modifier = Modifier.padding(start = 16.dp),
                checked = status.isFavourited == true,
                checkedColor = WoollyTheme.FavouriteColor,
                icon = Icons.Default.Star,
                contentDescription = if (status.isBoosted == true) "Remove from favourites" else "Add to favourites",
                counter = status.favouritesCount,
                onCheckedChange = { favourited ->
                    onStatusAction(
                        if (favourited) StatusAction.Favourite(status)
                        else StatusAction.UndoFavourite(status)
                    )
                }
            )
        }
    }
}

@Composable
private fun StatusAction(
    modifier: Modifier = Modifier,
    checked: Boolean,
    checkedColor: Color = LocalContentColor.current,
    icon: ImageVector,
    contentDescription: String,
    counter: Long,
    onCheckedChange: (Boolean) -> Unit
) {
    val color =
        if (checked) checkedColor
        else LocalContentColor.current.copy(alpha = 0.7f)

    Row(verticalAlignment = Alignment.CenterVertically) {
        IconToggleButton(
            modifier = modifier,
            checked = checked,
            onCheckedChange = onCheckedChange
        ) {
            Icon(
                icon,
                modifier = Modifier.size(24.dp),
                contentDescription = contentDescription,
                tint = color
            )
        }

        Text(
            text = counter.toString(),
            maxLines = 1,
            style = MaterialTheme.typography.caption,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StatusMediaList(
    modifier: Modifier = Modifier,
    media: List<Attachment>,
    isSensitive: Boolean
) {
    Column(modifier = modifier.fillMaxWidth()) {
        val displayableMedia = media.filter { it.previewUrl != null }
        displayableMedia.forEachIndexed { i, item ->
            key(item.attachmentId) {
                StatusMediaPreview(
                    media = item,
                    isSensitive = isSensitive
                )
            }

            if (i < displayableMedia.size - 1) {
                Spacer(modifier = Modifier.padding(bottom = 8.dp))
            }
        }
    }
}

@Composable
fun StatusMediaPreview(
    modifier: Modifier = Modifier,
    media: Attachment,
    isSensitive: Boolean
) {
    val uriHandler = LocalUriHandler.current
    val icon = when (media.type) {
        AttachmentType.Image -> null
        AttachmentType.Gifv -> Icons.Default.Gif
        AttachmentType.Video -> Icons.Default.PlayCircle
        AttachmentType.Audio -> Icons.Default.Mic
        AttachmentType.Unknown -> Icons.Default.Help
    }

    StatusMediaOverlay(
        icon = icon,
        contentDescription = media.description
    ) {
        StatusImage(
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(16 / 9f)
                .clip(MaterialTheme.shapes.medium)
                .clickable { uriHandler.openUri(media.url) },
            previewUrl = media.previewUrl ?: media.url,
            contentDescription = media.description,
            blurHash = media.blurHash,
            isSensitive = isSensitive
        )
    }
}

@Composable
fun StatusMediaOverlay(
    modifier: Modifier = Modifier,
    icon: ImageVector?,
    contentDescription: String?,
    content: @Composable () -> Unit
) {
    Box(modifier.fillMaxSize()) {
        content()

        if (icon != null) {
            Surface(
                shape = CircleShape,
                color = Color.Black.copy(alpha = 0.7f),
                modifier = Modifier
                    .size(48.dp)
                    .align(Alignment.Center)
            ) {
                Icon(
                    modifier = Modifier.padding(8.dp),
                    imageVector = icon,
                    contentDescription = contentDescription,
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun StatusImage(
    modifier: Modifier = Modifier,
    previewUrl: String,
    contentDescription: String?,
    blurHash: String?,
    isSensitive: Boolean
) {
    if (!isSensitive) {
        KamelImage(
            modifier = modifier,
            resource = lazyImageResource(previewUrl) {
                dispatcher = Dispatchers.IO
            },
            onLoading = {
                BlurHashImage(
                    modifier = modifier,
                    blurHash = blurHash,
                    contentDescription = contentDescription
                )
            },
            contentDescription = contentDescription,
            crossfade = true,
            animationSpec = tween(),
            contentScale = ContentScale.FillWidth
        )
    } else {
        BlurHashImage(
            modifier = modifier,
            blurHash = blurHash,
            contentDescription = contentDescription
        )
    }
}

@Composable
fun BlurHashImage(
    modifier: Modifier = Modifier,
    blurHash: String?,
    contentDescription: String?
) {
    val blurHashBitmap = remember(blurHash) {
        BlurHashDecoder.decode(blurHash, height = 32, width = 32)
    }

    when (blurHashBitmap) {
        null -> Spacer(modifier = modifier)
        else -> Image(
            modifier = modifier,
            bitmap = blurHashBitmap,
            contentDescription = contentDescription,
            contentScale = ContentScale.FillWidth
        )
    }
}
