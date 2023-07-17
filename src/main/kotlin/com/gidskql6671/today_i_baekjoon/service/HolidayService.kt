package com.gidskql6671.today_i_baekjoon.service

import com.gidskql6671.today_i_baekjoon.domain.Holiday
import com.gidskql6671.today_i_baekjoon.repository.HolidayRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate

@Service
class HolidayService(private val holidayRepository: HolidayRepository) {


    fun sync(start: LocalDate, end: LocalDate) {
        var date = start
        while (!date.isAfter(end)) {
            val dayOfWeek = date.dayOfWeek
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                holidayRepository.findByDate(date) ?: holidayRepository.save(Holiday(date = date))
            }
            date = date.plusDays(1)
        }
    }

    fun addHolidays(dates: List<LocalDate>) {
        val holidays: List<Holiday> = dates
            .stream()
            .filter { date -> !holidayRepository.existsByDate(date) }
            .map { Holiday(date = it) }
            .toList()

        holidayRepository.saveAll(holidays)
    }

    @Transactional
    fun removeHolidays(dates: List<LocalDate>) {
        holidayRepository.deleteByDateIn(dates)
    }

    fun getHolidaysBetweenDate(start: LocalDate, end: LocalDate): List<LocalDate> {
        return holidayRepository
            .findAllByDateBetween(start, end)
            .stream()
            .map(Holiday::date)
            .toList()
    }

    fun isHoliday(date: LocalDate): Boolean = holidayRepository.existsByDate(date)
}