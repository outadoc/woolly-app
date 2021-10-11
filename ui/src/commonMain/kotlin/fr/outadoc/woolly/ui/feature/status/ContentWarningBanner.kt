package fr.outadoc.woolly.ui.feature.status

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ContentWarningBanner(
    modifier: Modifier = Modifier,
    contentWarning: String,
    isCollapsed: Boolean,
    onToggle: () -> Unit
) {
    Card(modifier = modifier.clickable { onToggle() }) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f, fill = false),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Info,
                    contentDescription = "Content warning",
                    modifier = Modifier.padding(end = 16.dp)
                )

                Text(
                    modifier = Modifier.padding(end = 8.dp),
                    text = contentWarning,
                    style = MaterialTheme.typography.body2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            if (isCollapsed) {
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Expand post"
                )
            } else {
                Icon(
                    Icons.Default.ArrowDropUp,
                    contentDescription = "Collapse post"
                )
            }
        }
    }
}