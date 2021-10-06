package fr.outadoc.woolly.ui.feature.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Card
import fr.outadoc.woolly.ui.common.BlurHashImage
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

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
                        crossfade = true,
                        contentScale = ContentScale.FillWidth,
                        resource = lazyPainterResource(image),
                        contentDescription = "Link preview",
                        onLoading = {
                            val blurHash = card.blurHash
                            if (blurHash != null) {
                                BlurHashImage(
                                    blurHash = blurHash,
                                    contentDescription = "Link preview"
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

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.subtitle1
                )

                if (card.description.isNotBlank()) {
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = card.description,
                        style = MaterialTheme.typography.body2,
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                    )
                }
            }
        }
    }
}
