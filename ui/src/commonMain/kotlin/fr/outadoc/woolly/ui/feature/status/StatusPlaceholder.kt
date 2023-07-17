package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.material3.placeholder
import fr.outadoc.woolly.ui.common.FillFirstThenWrap
import fr.outadoc.woolly.ui.common.WoollyDefaults
import fr.outadoc.woolly.ui.common.WoollyTheme

@Composable
fun StatusPlaceholder(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .padding(end = 16.dp)
                .size(WoollyDefaults.AvatarSize)
                .clip(WoollyTheme.AvatarShape)
                .placeholder(visible = true)
        )

        Column {
            Column(modifier = Modifier.padding(bottom = 4.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp),
                    horizontalArrangement = FillFirstThenWrap,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .alignByBaseline()
                            .weight(1f, fill = false)
                            .padding(end = 8.dp)
                            .placeholder(visible = true),
                        text = "Lorem ipsum",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    Text(
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .alignByBaseline()
                            .placeholder(visible = true),
                        text = "Lorem",
                        style = MaterialTheme.typography.subtitle2
                    )
                }

                Text(
                    modifier = Modifier.placeholder(visible = true),
                    text = "@loremipsum",
                    style = MaterialTheme.typography.subtitle2,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .placeholder(visible = true),
                text = """
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                    incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,..
                """.trimIndent()
            )
        }
    }
}
