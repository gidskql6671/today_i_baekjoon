package com.gidskql6671.today_i_baekjoon.controller

import com.gidskql6671.today_i_baekjoon.domain.User
import com.gidskql6671.today_i_baekjoon.dto.ShowFineOfWeekModel
import com.gidskql6671.today_i_baekjoon.service.CommitService
import com.gidskql6671.today_i_baekjoon.service.HolidayService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

@Controller
@RequestMapping("/calendar")
class CalendarController(
    private val commitService: CommitService,
    private val holidayService: HolidayService
) {

    @GetMapping("/week")
    fun showCalendarOfWeek(
        model: Model,
        @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam("startDate") startDate: Optional<LocalDate>
    ): String {
        val today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate()
        val curDate = startDate.orElse(today)
        if (curDate.isAfter(today)) {
            return "redirect:/"
        }

        // NOTE 월요일 1, 일요일 7... Enum을 int로 쓴다는게 좋은 방법은 아니겠지만...
        val dayOfWeek = curDate.dayOfWeek.value
        val start = curDate.minusDays((dayOfWeek - 1).toLong())
        val end = start.plusDays(6)

        val holidays: List<LocalDate> = holidayService.getHolidaysBetweenDate(start, end)

        val isHolidays: MutableList<Boolean> = ArrayList()
        val dates: MutableList<LocalDate> = ArrayList()
        var date = start
        while (!date.isAfter(end)) {
            dates.add(date)
            isHolidays.add(holidays.contains(date))
            date = date.plusDays(1)
        }

        val solved: Map<User, List<Boolean>> = commitService.getSolvedListBetweenDate(start, end)
        val solvedList = convertKeyUserToUsername(solved)

        val data = ShowFineOfWeekModel(
            dates = dates,
            prevStartDate = start.minusDays(7),
            nextStartDate = if (today.isBefore(start.plusDays(7))) null else start.plusDays(7),
            solvedList = solvedList,
            isHolidays = isHolidays
        )

        model.addAttribute("data", data)

        return "fine_of_week"
    }

    private fun convertKeyUserToUsername(source: Map<User, List<Boolean>>): Map<String, List<Boolean>> {
        val result: MutableMap<String, List<Boolean>> = HashMap()

        source.forEach { (k: User, v: List<Boolean>) -> result[k.name] = v }

        return result
    }
}