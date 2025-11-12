package team.mediagroup.dto

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class EmployeeResponse(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    val departmentId: Int,
    val roleInCompany: String,
    val hiredAt: String,
    val isOnHoliday: Boolean
)
