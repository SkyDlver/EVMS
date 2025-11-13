package team.mediagroup.dto

import kotlinx.serialization.Serializable
import team.mediagroup.models.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class HolidayRequest(
    val employeeId: Int,
    @Serializable(with = LocalDateSerializer::class)
    val start: LocalDate,
    @Serializable(with = LocalDateSerializer::class)
    val end: LocalDate,
    val override10MonthRule: Boolean = false // HR can bypass 10-month restriction
)
