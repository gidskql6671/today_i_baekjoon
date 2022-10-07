package com.example.today_i_baekjoon.service;


import com.example.today_i_baekjoon.domain.Holiday;
import com.example.today_i_baekjoon.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HolidayService {

    private final HolidayRepository holidayRepository;

    public void sync(LocalDate start, LocalDate end) {
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            if (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                if (holidayRepository.findByDate(date).isEmpty()) {
                    holidayRepository.save(Holiday.builder().date(date).build());
                }
            }
        }
    }

    public void addHolidays(List<LocalDate> dates) {
        List<Holiday> holidays = dates
                .stream()
                .filter(date -> !holidayRepository.existsByDate(date))
                .map(date -> Holiday.builder().date(date).build())
                .toList();

        holidayRepository.saveAll(holidays);
    }

    @Transactional
    public void removeHolidays(List<LocalDate> dates) {
        System.out.println(dates);
        holidayRepository.deleteByDateIn(dates);
    }
}
