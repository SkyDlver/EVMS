package team.mediagroup.dto

import kotlinx.serialization.Serializable
import team.mediagroup.models.Role

@Serializable
data class UserResponse(
    val id: Int,
    val username: String,
    val role: String,
    val departmentId: Int?
)
