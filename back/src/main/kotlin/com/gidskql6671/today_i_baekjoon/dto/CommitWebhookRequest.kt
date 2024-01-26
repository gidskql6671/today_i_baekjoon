package com.gidskql6671.today_i_baekjoon.dto

import java.time.ZonedDateTime

data class CommitWebhookRequest(
    val pusher: Pusher,
    val commits: List<Commit> = ArrayList()
) {
    data class Commit (val url: String, val message: String, val timestamp: ZonedDateTime)
    data class Pusher (val name: String, val email: String)
}
