package com.gidskql6671.today_i_baekjoon.dto

import java.time.LocalDate

data class HolidaysRequest(
    val holidays: List<LocalDate> = arrayListOf()
)