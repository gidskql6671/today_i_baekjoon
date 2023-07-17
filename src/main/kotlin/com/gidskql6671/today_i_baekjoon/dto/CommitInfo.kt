package com.gidskql6671.today_i_baekjoon.dto

import com.gidskql6671.today_i_baekjoon.domain.Commit
import java.time.LocalDate

data class CommitInfo(
    val commitDate: LocalDate,
    val commitUrl: String,
    val username: String,
    val problemTitle: String,
    val problemRank: String
)

fun commitInfoFromEntity(commit: Commit) = CommitInfo(
    commitDate = commit.commitDate,
    commitUrl = commit.commitUrl,
    username = commit.user.username,
    problemTitle = commit.problemTitle,
    problemRank = commit.problemRank
)