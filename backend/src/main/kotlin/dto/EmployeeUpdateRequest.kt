package team.mediagroup.dto

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class EmployeeUpdateRequest(
    val firstName: String,
    val lastName: String,
    val middleName: String? = null,
    val departmentId: Int?,
    val roleInCompany: String,
    @Contextual
    val hiredAt: LocalDate? = null,
    val isOnHoliday: Boolean = false
)
