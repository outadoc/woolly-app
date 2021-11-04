package fr.outadoc.woolly.ui.feature.tags

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ListItem
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Tag
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.accompanist.placeholder.material.placeholder
import fr.outadoc.woolly.ui.MR
import fr.outadoc.woolly.ui.strings.stringResource

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HashtagPlaceholder(modifier: Modifier = Modifier) {
    ListItem(
        modifier = modifier,
        icon = {
            Icon(
                modifier = Modifier.placeholder(visible = true),
                imageVector = Icons.Default.Tag,
                contentDescription = stringResource(MR.strings.hashtagTimeline_icon_cd)
            )
        }
    ) {
        Text(
            modifier = Modifier.placeholder(visible = true),
            text = "#LoremIpsum"
        )
    }
}
