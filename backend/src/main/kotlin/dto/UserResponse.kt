package team.mediagroup.dto

import team.mediagroup.models.Role

data class UserResponse(
    val id: Int,
    val username: String,
    val role: String,
    val departmentId: Int?
)
