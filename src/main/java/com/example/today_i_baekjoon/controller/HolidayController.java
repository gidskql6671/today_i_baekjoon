package com.example.today_i_baekjoon.controller;

import com.example.today_i_baekjoon.dto.AddHolidaysBetweenRequest;
import com.example.today_i_baekjoon.dto.HolidaysRequest;
import com.example.today_i_baekjoon.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/holiday")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    @PostMapping
    public void addHolidays(@Valid @RequestBody HolidaysRequest request) {
        holidayService.addHolidays(request.getHolidays());
    }

    @PostMapping("/range")
    public void addHolidaysBetween(@Valid @RequestBody AddHolidaysBetweenRequest request) {
        LocalDate start = request.getStart(), end = request.getEnd();
        List<LocalDate> dates = new ArrayList<>();
        for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
            dates.add(date);
        }
        
        holidayService.addHolidays(dates);
    }
    
    @DeleteMapping
    public void removeHolidays(@Valid @RequestBody HolidaysRequest request) {
        holidayService.removeHolidays(request.getHolidays());
    }
}
