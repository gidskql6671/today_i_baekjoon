package com.gidskql6671.today_i_baekjoon.dto

import java.time.LocalDate

data class ShowFineOfWeekModel(
    val dates: List<LocalDate>,
    val isHolidays: List<Boolean>,
    val prevStartDate: LocalDate,
    val nextStartDate: LocalDate?,
    val solvedList: Map<String, List<Boolean>>
)
