package com.gidskql6671.today_i_baekjoon.repository

import com.gidskql6671.today_i_baekjoon.domain.Holiday
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface HolidayRepository: JpaRepository<Holiday, Long> {

    fun findByDate(date: LocalDate): Holiday?

    fun findAllByDateBetween(start: LocalDate, end: LocalDate): List<Holiday>

    fun existsByDate(date: LocalDate): Boolean

    fun deleteByDateIn(date: List<LocalDate>)
}