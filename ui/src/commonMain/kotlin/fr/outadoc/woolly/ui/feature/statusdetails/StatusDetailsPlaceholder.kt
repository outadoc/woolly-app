package fr.outadoc.woolly.ui.feature.statusdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.placeholder.material3.placeholder
import fr.outadoc.woolly.ui.common.BulletSeparator
import fr.outadoc.woolly.ui.feature.account.AccountPlaceholder

@Composable
fun StatusDetailsPlaceholder(
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AccountPlaceholder(
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Text(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .placeholder(visible = true),
            style = MaterialTheme.typography.body1,
            text = """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor
                incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam,..
            """.trimIndent()
        )

        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                modifier = Modifier
                    .alignByBaseline()
                    .placeholder(visible = true),
                text = "Lorem",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )

            BulletSeparator(modifier = Modifier.placeholder(visible = true))

            Text(
                modifier = Modifier
                    .alignByBaseline()
                    .placeholder(visible = true),
                text = "Lorem ipsum dolor",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle2
            )
        }
    }
}
