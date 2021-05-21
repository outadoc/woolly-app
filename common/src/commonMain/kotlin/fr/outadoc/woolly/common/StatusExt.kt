package fr.outadoc.woolly.common

import fr.outadoc.mastodonk.api.entity.Account

val Account.displayNameOrAcct: String
    get() = if (displayName.isNotBlank()) displayName else "@$acct"
