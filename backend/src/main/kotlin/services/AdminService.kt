package team.mediagroup.services

import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import team.mediagroup.models.*
import team.mediagroup.dto.UserCreateRequest
import team.mediagroup.dto.UserUpdateRequest
import team.mediagroup.dto.UserResponse

class AdminService(private val authService: AuthService) {

    // List all users
    fun getAllUsers(
        role: String? = null,
        departmentId: Int? = null,
        username: String? = null
    ): List<UserResponse> = transaction {
        // Build the query dynamically
        var query = Users.selectAll()

        if (role != null) {
            query = query.andWhere { Users.role eq Role.valueOf(role.uppercase()) }
        }

        if (departmentId != null) {
            query = query.andWhere { Users.departmentId eq departmentId }
        }

        if (!username.isNullOrBlank()) {
            query = query.andWhere { Users.username like "%$username%" }
        }

        query.map {
            UserResponse(
                id = it[Users.id].value,
                username = it[Users.username],
                role = it[Users.role].name,
                departmentId = it[Users.departmentId]
            )
        }
    }

    fun getUserById(id: Int): UserResponse? = transaction {
        User.findById(id)?.let {
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
