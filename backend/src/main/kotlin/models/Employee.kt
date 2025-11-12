package team.mediagroup.models

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object Employees : IntIdTable() {
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val middleName = varchar("middle_name", 50).nullable()
    val departmentId = reference("department_id", Departments) // <-- add this
    val roleInCompany = varchar("role_in_company", 50)
    val hiredAt = date("hired_at")
    val isOnHoliday = bool("is_on_holiday").default(false)
}

class Employee(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Employee>(Employees)

    var firstName by Employees.firstName
    var lastName by Employees.lastName
    var middleName by Employees.middleName
    var departmentId by Employees.departmentId // <-- add this
    var roleInCompany by Employees.roleInCompany
    var hiredAt by Employees.hiredAt
    var isOnHoliday by Employees.isOnHoliday
}
