package team.mediagroup.mappers

import team.mediagroup.models.UserPrincipal
import team.mediagroup.dto.UserResponse

fun UserPrincipal.toResponse(): UserResponse = UserResponse(
    id = id,
    username = username,
    role = role.name,
    departmentId = departmentId
)
