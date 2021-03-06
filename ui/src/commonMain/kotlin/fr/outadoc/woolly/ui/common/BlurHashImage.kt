package fr.outadoc.woolly.ui.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import com.wolt.blurhashkt.BlurHashDecoder

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
            contentScale = ContentScale.Crop
        )
    }
}
