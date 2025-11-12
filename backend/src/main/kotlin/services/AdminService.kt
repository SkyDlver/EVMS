package team.mediagroup.services

import org.jetbrains.exposed.sql.transactions.transaction
import team.mediagroup.models.*
import team.mediagroup.dto.UserCreateRequest
import team.mediagroup.dto.UserUpdateRequest
import team.mediagroup.dto.UserResponse

class AdminService(private val authService: AuthService) {

    // List all users
    fun getAllUsers(): List<UserResponse> = transaction {
        User.all().map {
            UserResponse(
                id = it.id.value,
                username = it.username,
                role = it.role.name,
                departmentId = it.departmentId
            )
        }
    }

    // Create a new user
    fun createUser(request: UserCreateRequest) = transaction {
        val hashed = authService.hashPassword(request.password)
        User.new {
            username = request.username
            passwordHash = hashed
            role = request.role
            departmentId = request.departmentId
        }
    }

    fun updateUser(id: Int, request: UserUpdateRequest) = transaction {
        val user = User.findById(id) ?: return@transaction
        user.username = request.username
        user.role = request.role
        user.departmentId = request.departmentId
        if (!request.password.isNullOrBlank()) {
            user.passwordHash = authService.hashPassword(request.password)
        }
    }

    // Optional: delete user
    fun deleteUser(id: Int) = transaction {
        val user = User.findById(id) ?: return@transaction
        user.delete()
    }
}
