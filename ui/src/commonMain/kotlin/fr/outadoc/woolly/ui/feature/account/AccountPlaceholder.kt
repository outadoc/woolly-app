package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.*
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.material.placeholder
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.common.WoollyTheme

@Composable
fun AccountPlaceholder(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(WoollyDefaults.AvatarSize)
                .clip(WoollyTheme.AvatarShape)
                .placeholder(visible = true)
        )

        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .alignByBaseline()
                        .fillMaxWidth(0.8f)
                        .placeholder(visible = true),
                    text = "Lorem Ipsum",
                    style = MaterialTheme.typography.subtitle1,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            Text(
                modifier = Modifier.placeholder(visible = true),
                text = "@loremipsum",
                style = MaterialTheme.typography.subtitle2,
                color = LocalContentColor.current.copy(alpha = ContentAlpha.medium),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
