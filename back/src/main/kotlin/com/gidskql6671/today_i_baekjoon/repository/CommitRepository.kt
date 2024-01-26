package com.gidskql6671.today_i_baekjoon.repository

import com.gidskql6671.today_i_baekjoon.domain.Commit
import com.gidskql6671.today_i_baekjoon.domain.User
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface CommitRepository: JpaRepository<Commit, Long> {

    fun existsByCommitUrl(commitUrl: String): Boolean

    fun findAllByCommitDateBetween(start: LocalDate, end: LocalDate): List<Commit>
    fun findAllByCommitDateBetweenAndUser(start: LocalDate, end: LocalDate, user: User): List<Commit>

    fun findAllByCommitDate(date: LocalDate): List<Commit>
}