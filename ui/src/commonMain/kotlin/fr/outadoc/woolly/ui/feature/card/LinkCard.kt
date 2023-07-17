package fr.outadoc.woolly.ui.feature.card

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Card
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.common.BlurHashImage
import fr.outadoc.woolly.ui.strings.stringResource
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LinkCard(
    modifier: Modifier = Modifier,
    card: Card.Link,
    defaultIcon: @Composable () -> Unit = {},
    onClick: () -> Unit = {},
    iconWidth: Dp = 80.dp
) {
    Card(
        modifier = modifier,
        onClick = onClick
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Row(
                modifier = Modifier.width(iconWidth),
                horizontalArrangement = Arrangement.Center
            ) {
                val image = card.image
                if (image != null) {
                    KamelImage(
                        animationSpec = tween(),
                        contentScale = ContentScale.FillWidth,
                        resource = asyncPainterResource(image),
                        contentDescription = stringResource(MR.strings.status_preview_cd),
                        onLoading = {
                            val blurHash = card.blurHash
                            if (blurHash != null) {
                                BlurHashImage(
                                    blurHash = blurHash,
                                    contentDescription = stringResource(MR.strings.status_preview_cd)
                                )
                            } else {
                                CircularProgressIndicator()
                            }
                        },
                        onFailure = {
                            defaultIcon()
                        }
                    )
                } else {
                    defaultIcon()
                }
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.subtitle1
                )

                if (card.description.isNotBlank()) {
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = card.description,
                        style = MaterialTheme.typography.body2,
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                        maxLines = 3
                    )
                }
            }
        }
    }
}
