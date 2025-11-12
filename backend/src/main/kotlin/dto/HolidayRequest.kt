package team.mediagroup.dto

import java.time.LocalDate

data class HolidayRequest(
    val employeeId: Int,
    val start: LocalDate,
    val end: LocalDate,
    val override10MonthRule: Boolean = false // HR can bypass 10-month restriction
)
