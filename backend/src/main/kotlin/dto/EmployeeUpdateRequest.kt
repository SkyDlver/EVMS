package team.mediagroup.dto

import kotlinx.serialization.Serializable
import team.mediagroup.models.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class EmployeeUpdateRequest(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val departmentId: Int?,
    val roleInCompany: String,
    @Serializable(with = LocalDateSerializer::class)
    val hiredAt: LocalDate? = null,
    val isOnHoliday: Boolean = false
)
