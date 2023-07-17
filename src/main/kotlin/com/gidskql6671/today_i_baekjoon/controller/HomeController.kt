package com.gidskql6671.today_i_baekjoon.controller

import com.gidskql6671.today_i_baekjoon.domain.User
import com.gidskql6671.today_i_baekjoon.dto.CommitInfo
import com.gidskql6671.today_i_baekjoon.exception.UserNotFoundException
import com.gidskql6671.today_i_baekjoon.service.CommitService
import com.gidskql6671.today_i_baekjoon.service.HolidayService
import com.gidskql6671.today_i_baekjoon.service.UserService
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*
import java.util.function.Consumer

@Controller
@RequestMapping("/")
class HomeController(
    private val commitService: CommitService,
    private val userService: UserService,
    private val holidayService: HolidayService
) {


    @GetMapping("/")
    fun homepage(
        model: Model,
        @DateTimeFormat(pattern = "yyyy-MM-dd") @RequestParam date: Optional<LocalDate>
    ): String? {
        val today = ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate()
        val curDate = date.orElse(today)
        if (curDate.isAfter(today)) {
            return "redirect:/"
        }

        val commits = commitService.getAllCommitsByDate(curDate)
        val users = userService.findAll()

        val userCommits = mappedCommitToUser(users, commits)
        val namesMappedUsername: MutableMap<String, String> = HashMap()

        users.forEach(Consumer { namesMappedUsername[it.username] = it.name })

        val isHoliday = holidayService.isHoliday(curDate)

        model.addAttribute("namesMappedUsername", namesMappedUsername)
        model.addAttribute("userCommits", userCommits)
        model.addAttribute("isHoliday", isHoliday)
        model.addAttribute("curDate", curDate)
        // NOTE thymeleaf dates 라이브러리 사용이 안되는 버그가 있어서 여기서 다 넣어줌. 뭐가 문젠지 모르겠음;;
        model.addAttribute("prevDate", curDate.minusDays(1))
        if (curDate.isBefore(today)) {
            model.addAttribute("nextDate", curDate.plusDays(1))
        }

        return "index"
    }

    @GetMapping("/user/{username}")
    fun detail(
        model: Model,
        @PathVariable("username") username: String,
        @DateTimeFormat(pattern = "yyyy-MM") @RequestParam("yearMonth") yearMonthReq: Optional<YearMonth>
    ): String? {
        val todayYearMonth = YearMonth.now(ZoneId.of("Asia/Seoul"))
        val yearMonth = yearMonthReq.orElse(todayYearMonth)
        if (yearMonth.isAfter(todayYearMonth)) {
            return "redirect:/user/$username"
        }

        val start = yearMonth.atDay(1)
        val end = yearMonth.atEndOfMonth()

        val user = userService.findUserByUsername(username) ?: throw UserNotFoundException()

        val commits = commitService.getAllCommitsBetweenDate(start, end, user)
        val calendar = getCalendarList(yearMonth)

        val dateCommits: Map<Int, List<CommitInfo>> = mappedCommitToDate(commits)
        val holidays = holidayService
            .getHolidaysBetweenDate(start, end)
            .stream()
            .map { obj: LocalDate -> obj.dayOfMonth }
            .toList()

        model.addAttribute("name", user.name)
        model.addAttribute("dateCommits", dateCommits)
        model.addAttribute("calendar", calendar)
        model.addAttribute("holidays", holidays)

        model.addAttribute("yearMonth", yearMonth)
        model.addAttribute("prevYearMonth", yearMonth.minusMonths(1))
        if (yearMonth.isBefore(todayYearMonth)) {
            model.addAttribute("nextYearMonth", yearMonth.plusMonths(1))
        }

        return "detail"
    }


    private fun mappedCommitToUser(
        users: List<User>,
        commitInfos: List<CommitInfo>
    ): Map<String, MutableList<CommitInfo>> {

        val result: MutableMap<String, MutableList<CommitInfo>> = HashMap()

        users.forEach(Consumer{ result[it.username] = ArrayList<CommitInfo>() })
        commitInfos.forEach(Consumer { result[it.username]!!.add(it) })

        return result
    }

    private fun mappedCommitToDate(commitInfos: List<CommitInfo>): Map<Int, MutableList<CommitInfo>> {
        val result: MutableMap<Int, MutableList<CommitInfo>> = HashMap()

        commitInfos
            .forEach(Consumer { commit: CommitInfo ->
                val date: Int = commit.commitDate.dayOfMonth

                result
                    .computeIfAbsent(date) { ArrayList() }
                    .add(commit)
            })

        return result
    }

    private fun getCalendarList(yearMonth: YearMonth): List<List<Int?>> {
        val calendar: MutableList<List<Int?>> = ArrayList()

        val start = yearMonth.atDay(1)
        val endDate = yearMonth.atEndOfMonth().dayOfMonth

        val startDateOfCalendar = 1 - start.dayOfWeek.value % 7

        var dateCount = endDate - startDateOfCalendar
        dateCount = ((dateCount - 1) / 7 + 1) * 7

        var i = 0
        while (i < dateCount) {
            val week: MutableList<Int?> = ArrayList()

            for (j in i until i + 7) {
                val curDate = startDateOfCalendar + j
                if (curDate in 1..endDate) {
                    week.add(curDate)
                } else {
                    week.add(null)
                }
            }
            calendar.add(week)

            i += 7
        }
        return calendar
    }
}