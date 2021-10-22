package fr.outadoc.woolly.common.feature.media

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize
import fr.outadoc.mastodonk.api.entity.Attachment

@Parcelize
data class ImageAttachment(
    val url: String,
    val contentDescription: String?,
    val blurHash: String?,
    val ratio: Double?
) : Parcelable

fun Attachment.Image.toAppImage() = ImageAttachment(
    url = url,
    contentDescription = description,
    blurHash = blurHash,
    ratio = meta.original?.aspect
)
