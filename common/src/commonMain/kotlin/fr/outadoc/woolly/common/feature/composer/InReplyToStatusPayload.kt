package fr.outadoc.woolly.common.feature.composer

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
data class InReplyToStatusPayload(
    val statusId: String,
    val acct: String
) : Parcelable
