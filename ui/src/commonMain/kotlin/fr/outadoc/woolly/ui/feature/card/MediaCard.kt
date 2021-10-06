package fr.outadoc.woolly.ui.feature.card

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Card
import fr.outadoc.woolly.ui.common.BlurHashImage
import io.kamel.image.KamelImage
import io.kamel.image.lazyPainterResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MediaCard(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    card: Card
) {
    val image = card.image ?: return
    val aspectRatio = card.aspectRatio ?: 16 / 9f

    Card(modifier = modifier, onClick = onClick) {
        Column(modifier = Modifier.fillMaxWidth()) {
            KamelImage(
                modifier = Modifier.aspectRatio(aspectRatio),
                crossfade = true,
                contentScale = ContentScale.FillWidth,
                resource = lazyPainterResource(image),
                contentDescription = "Link preview",
                onLoading = {
                    val blurHash = card.blurHash
                    if (blurHash != null) {
                        BlurHashImage(
                            modifier = Modifier.aspectRatio(aspectRatio),
                            blurHash = blurHash,
                            contentDescription = "Link preview"
                        )
                    } else {
                        Row(
                            modifier = Modifier.aspectRatio(aspectRatio),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            )

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

                card.providerName?.let { providerName ->
                    Text(
                        modifier = Modifier.padding(top = 8.dp),
                        text = "Source: $providerName",
                        style = MaterialTheme.typography.body2,
                        color = LocalContentColor.current.copy(alpha = ContentAlpha.medium)
                    )
                }
            }
        }
    }
}

private val Card.aspectRatio: Float?
    get() {
        val height = height?.toFloat()
        val width = width?.toFloat()
        return if (height != null && width != null) width / height
        else null
    }
