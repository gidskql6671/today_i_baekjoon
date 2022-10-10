package com.example.today_i_baekjoon.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddHolidaysBetweenRequest {
	private LocalDate start;
	private LocalDate end;
}
