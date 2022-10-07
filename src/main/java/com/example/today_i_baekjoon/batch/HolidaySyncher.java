package com.example.today_i_baekjoon.batch;

import com.example.today_i_baekjoon.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class HolidaySyncher implements ApplicationListener<ApplicationStartedEvent> {

    private final HolidayService holidayService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {
        holidayService.sync(LocalDate.of(2022, 1, 1), LocalDate.of(2023, 12,  31));
    }
}
