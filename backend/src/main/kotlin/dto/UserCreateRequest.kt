package team.mediagroup.dto

import kotlinx.serialization.Serializable
import team.mediagroup.models.Role

@Serializable
data class UserCreateRequest(
    val username: String,
    val password: String,
    val role: Role,
    val departmentId: Int
)