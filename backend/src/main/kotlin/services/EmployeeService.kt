package team.mediagroup.services

import org.jetbrains.exposed.dao.id.EntityID
import team.mediagroup.models.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import team.mediagroup.dto.EmployeeResponse
import team.mediagroup.dto.EmployeeUpdateRequest
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import team.mediagroup.models.HolidayHistories.employee

class EmployeeService {

    fun getEmployeesForUser(user: UserPrincipal): List<EmployeeResponse> = transaction {
        val query = when (user.role) {
            Role.ADMIN -> Employees.selectAll()
            Role.HR, Role.VIEWER -> {
                val deptId = user.departmentId ?: return@transaction emptyList()
                Employees.select { Employees.departmentId eq deptId }
            }
        }

        query.map { row ->
            EmployeeResponse(
                id = row[Employees.id].value,
                firstName = row[Employees.firstName],
                lastName = row[Employees.lastName],
                middleName = row[Employees.middleName],
                departmentId = row[Employees.departmentId].value,
                roleInCompany = row[Employees.roleInCompany],
                hiredAt = row[Employees.hiredAt].toString(), // or format as needed
                isOnHoliday = row[Employees.isOnHoliday]
            )
        }
    }

    fun getEmployeeById(id: Int): Employee? = transaction {
        Employee.findById(id)
    }


    // Accept an Employee entity (routes pass the entity) and compare department ids
    fun canView(user: UserPrincipal, employee: Employee): Boolean {
        return when (user.role) {
            Role.ADMIN -> true
            Role.HR, Role.VIEWER -> user.departmentId == employee.departmentId.value
        }
    }

    fun canEdit(user: UserPrincipal, employee: Employee): Boolean {
        return when (user.role) {
            Role.ADMIN -> true
            Role.HR -> user.departmentId == employee.departmentId.value
            else -> false
        }
    }

    // Update in-place when caller provides the Employee entity
    fun updateEmployee(employee: Employee, request: EmployeeUpdateRequest): EmployeeResponse = transaction {
        val deptEntityId = if (request.departmentId != null) EntityID(request.departmentId, Departments) else employee.departmentId
        val middleCond = if (request.middleName.isNullOrBlank())
            Employees.middleName.isNull() or (Employees.middleName eq "")
        else
            Employees.middleName eq request.middleName

        val dupExists = Employees.select {
            (Employees.firstName eq request.firstName) and
                    (Employees.lastName eq request.lastName) and
                    middleCond and
                    (Employees.departmentId eq deptEntityId) and
                    (Employees.id neq employee.id.value)
        }.limit(1).any()

        if (dupExists) throw IllegalArgumentException("Duplicate employee exists in the same department")

        employee.firstName = request.firstName
        employee.lastName = request.lastName
        employee.middleName = request.middleName
        // update departmentId only if provided
        if (request.departmentId != null) {
            employee.departmentId = EntityID(request.departmentId, Departments)
        }
        employee.roleInCompany = request.roleInCompany
        employee.isOnHoliday = request.isOnHoliday

        EmployeeResponse(
            id = employee.id.value,
            firstName = employee.firstName,
            lastName = employee.lastName,
            middleName = employee.middleName,
            departmentId = employee.departmentId.value,
            roleInCompany = employee.roleInCompany,
            hiredAt = employee.hiredAt.toString(),
            isOnHoliday = employee.isOnHoliday
        )
    }

    fun createEmployee(request: EmployeeUpdateRequest): EmployeeResponse = transaction {
        val deptId = request.departmentId ?: throw IllegalArgumentException("departmentId is required")

        // Duplicate check: same first, last, middle (nullable), same department
        val dupExists = Employees.select {
            (Employees.firstName eq request.firstName) and
                    (Employees.lastName eq request.lastName) and
                    (if (request.middleName.isNullOrBlank())
                        Employees.middleName.isNull() or (Employees.middleName eq "")
                    else
                        Employees.middleName eq request.middleName) and
                    (Employees.departmentId eq EntityID(deptId, Departments)) // must wrap as EntityID here
        }.limit(1).any()

        if (dupExists) throw IllegalArgumentException("Duplicate employee exists in the same department")

        // Create new employee
        val employee = Employee.new {
            firstName = request.firstName
            lastName = request.lastName
            middleName = request.middleName
            departmentId = EntityID(deptId, Departments) // correct usage here
            roleInCompany = request.roleInCompany
            hiredAt = request.hiredAt ?: java.time.LocalDate.now()
            isOnHoliday = request.isOnHoliday
        }

        EmployeeResponse(
            id = employee.id.value,
            firstName = employee.firstName,
            lastName = employee.lastName,
            middleName = employee.middleName,
            departmentId = employee.departmentId.value,
            roleInCompany = employee.roleInCompany,
            hiredAt = employee.hiredAt.toString(),
            isOnHoliday = employee.isOnHoliday
        )
    }


    fun deleteEmployee(employeeId: Int) = transaction {
        Employee.findById(employeeId)?.delete()
    }
}
