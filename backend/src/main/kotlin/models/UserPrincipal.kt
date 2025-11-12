package team.mediagroup.models

import team.mediagroup.models.Role

data class UserPrincipal(
    val id: Int,
    val role: Role,
    val departmentId: Int?
)
