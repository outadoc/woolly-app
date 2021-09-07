package fr.outadoc.woolly.common.feature.composer

import com.arkivanov.decompose.statekeeper.Parcelable
import com.arkivanov.decompose.statekeeper.Parcelize

@Parcelize
data class InReplyToStatusPayload(
    val statusId: String,
    val acct: String
) : Parcelable
