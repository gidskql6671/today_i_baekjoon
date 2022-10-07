package com.example.today_i_baekjoon.service;


import com.example.today_i_baekjoon.domain.Holiday;
import com.example.today_i_baekjoon.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public void sync(LocalDate start, LocalDate end) {
        for (LocalDate date = start; date.isBefore(end); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                if (holidayRepository.findByDate(date).isEmpty()) {
                    holidayRepository.save(Holiday.builder().date(date).build());
                }
            }
        }
    }
}
