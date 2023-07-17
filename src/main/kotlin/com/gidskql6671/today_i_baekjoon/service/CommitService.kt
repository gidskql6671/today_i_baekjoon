package com.gidskql6671.today_i_baekjoon.service

import com.gidskql6671.today_i_baekjoon.domain.Commit
import com.gidskql6671.today_i_baekjoon.domain.User
import com.gidskql6671.today_i_baekjoon.dto.CommitInfo
import com.gidskql6671.today_i_baekjoon.dto.CommitWebhookRequest
import com.gidskql6671.today_i_baekjoon.dto.commitInfoFromEntity
import com.gidskql6671.today_i_baekjoon.repository.CommitRepository
import com.gidskql6671.today_i_baekjoon.repository.UserRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.function.Consumer
import java.util.regex.Pattern
import java.util.stream.Collectors

@Service
class CommitService(
    private val commitRepository: CommitRepository,
    private val userRepository: UserRepository
) {
    private val pattern = Pattern.compile("^\\[(?<rank>.*)] Title: (?<title>.*), Time:.*")

    fun addCommits(user: User, commitRequests: List<CommitWebhookRequest.Commit>) {
        val commits = commitRequests
            .stream()
            .filter { !commitRepository.existsByCommitUrl(it.url) }
            .filter { pattern.matcher(it.message).matches() }
            .map {
                var rank = ""
                var title = ""

                val matcher = pattern.matcher(it.message)
                if (matcher.matches()) {
                    rank = matcher.group("rank")
                    title = matcher.group("title")
                }

                Commit(
                    user = user,
                    commitDate = it.timestamp.toLocalDate(),
                    commitUrl = it.url,
                    problemTitle = title,
                    problemRank = rank
                )
            }
            .toList()

        commitRepository.saveAll(commits)
    }

    fun getAllCommitsByDate(date: LocalDate): List<CommitInfo> {
        return commitRepository
            .findAllByCommitDate(date)
            .stream()
            .map { commitInfoFromEntity(it) }
            .collect(Collectors.toList())
    }

    fun getAllCommitsBetweenDate(start: LocalDate, end: LocalDate): List<CommitInfo> {
        return commitRepository
            .findAllByCommitDateBetween(start, end)
            .stream()
            .map { commitInfoFromEntity(it) }
            .collect(Collectors.toList())
    }

    fun getAllCommitsBetweenDate(start: LocalDate, end: LocalDate, user: User): List<CommitInfo> {
        return commitRepository
            .findAllByCommitDateBetweenAndUser(start, end, user)
            .stream()
            .map { commitInfoFromEntity(it) }
            .collect(Collectors.toList())
    }

    fun getSolvedListBetweenDate(start: LocalDate, end: LocalDate): Map<User, MutableList<Boolean>> {
        val users = userRepository.findAll()
        val commits = commitRepository.findAllByCommitDateBetween(start, end)
        val result: MutableMap<User, MutableList<Boolean>> = HashMap()

        users.forEach(Consumer { user: User -> result[user] = ArrayList() })

        var date = start
        while (!date.isAfter(end)) {
            val finalDate = date

            for (user in result.keys) {
                val solved = commits
                    .stream()
                    .filter { it.commitDate.isEqual(finalDate) && it.user.username == user.username }
                    .findAny()

                result[user]!!.add(solved.isPresent)
            }

            date = date.plusDays(1)
        }
        return result
    }
}