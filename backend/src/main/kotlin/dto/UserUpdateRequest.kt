package team.mediagroup.dto

import kotlinx.serialization.Serializable
import team.mediagroup.models.Role

@Serializable
enum class Role { ADMIN, HR, VIEWER }

@Serializable
data class UserUpdateRequest(
    val username: String,
    val password: String?,
    val role: Role,
    val departmentId: Int
)