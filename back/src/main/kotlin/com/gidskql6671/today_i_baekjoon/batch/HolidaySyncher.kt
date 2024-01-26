package com.gidskql6671.today_i_baekjoon.batch

import com.gidskql6671.today_i_baekjoon.service.HolidayService
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class HolidaySyncher(private val holidayService: HolidayService): ApplicationListener<ApplicationStartedEvent> {

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        holidayService.sync(LocalDate.of(2023, 1, 1), LocalDate.of(2023, 12, 31))
    }
}