package fr.outadoc.woolly.ui.feature.account

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import fr.outadoc.mastodonk.api.entity.Emoji
import fr.outadoc.mastodonk.api.entity.Field
import fr.outadoc.woolly.ui.richtext.RichText

@Composable
fun AccountFields(
    modifier: Modifier = Modifier,
    fields: List<Field>,
    emojis: List<Emoji>
) {
    Column(modifier = modifier) {
        fields.forEach { field ->
            Row {
                Text(
                    text = field.name,
                    style = LocalTextStyle.current.copy(fontWeight = FontWeight.Bold)
                )

                RichText(
                    modifier = Modifier.padding(start = 16.dp),
                    text = field.value,
                    emojis = emojis
                )
            }
        }
    }
}