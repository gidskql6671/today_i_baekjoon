package com.example.today_i_baekjoon.controller;

import com.example.today_i_baekjoon.dto.HolidaysRequest;
import com.example.today_i_baekjoon.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/holiday")
@RequiredArgsConstructor
public class HolidayController {

    private final HolidayService holidayService;

    @PostMapping
    public void addHolidays(@Valid @RequestBody HolidaysRequest request) {
        holidayService.addHolidays(request.getHolidays());
    }

    @DeleteMapping
    public void removeHolidays(@Valid @RequestBody HolidaysRequest request) {
        holidayService.removeHolidays(request.getHolidays());
    }
}
