package team.mediagroup.models

import kotlinx.serialization.Serializable

@Serializable
data class UserPrincipal(
    val id: Int,
    val role: Role,
    val departmentId: Int? = null,
    val username: String
)
