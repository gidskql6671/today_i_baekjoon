package com.gidskql6671.today_i_baekjoon.controller

import com.gidskql6671.today_i_baekjoon.dto.AddHolidaysBetweenRequest
import com.gidskql6671.today_i_baekjoon.dto.HolidaysRequest
import com.gidskql6671.today_i_baekjoon.service.HolidayService
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/api/holiday")
class HolidayController(private val holidayService: HolidayService) {

    @PostMapping
    fun addHolidays(@RequestBody request: HolidaysRequest) {
        holidayService.addHolidays(request.holidays)
    }

    @PostMapping("/range")
    fun addHolidaysBetween(@RequestBody request: AddHolidaysBetweenRequest) {
        val start: LocalDate = request.start
        val end: LocalDate = request.end

        val dates: MutableList<LocalDate> = ArrayList()

        var date = start
        while (!date.isAfter(end)) {
            dates.add(date)
            date = date.plusDays(1)
        }

        holidayService.addHolidays(dates)
    }

    @DeleteMapping
    fun removeHolidays(@RequestBody request: HolidaysRequest) {
        holidayService.removeHolidays(request.holidays)
    }
}