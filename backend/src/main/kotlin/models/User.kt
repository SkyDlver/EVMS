package team.mediagroup.models

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

enum class Role { ADMIN, HR, VIEWER }

object Users : IntIdTable() {
    val username = varchar("username", 50).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val role = enumerationByName("role", 10, Role::class)
    val departmentId = integer("department_id").references(Departments.id).nullable()
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)
    var username by Users.username
    var passwordHash by Users.passwordHash
    var role by Users.role
    var departmentId by Users.departmentId
}
