package com.example.today_i_baekjoon.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;


@ToString
@Builder
@Getter
public class ShowFineOfWeekModel {
	private List<LocalDate> dates;
	private List<Boolean> isHolidays;
	private LocalDate prevStartDate;
	private LocalDate nextStartDate;
	private Map<String, Integer> fines;
	private Map<String, List<Boolean>> solvedList;
}
