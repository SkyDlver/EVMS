package team.mediagroup.services

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.decimalParam
import team.mediagroup.dto.EmployeeResponse
import team.mediagroup.dto.EmployeeUpdateRequest
import team.mediagroup.mappers.toResponse
import team.mediagroup.models.Department
import team.mediagroup.models.Employee
import team.mediagroup.models.Role
import team.mediagroup.models.UserPrincipal
import team.mediagroup.repositories.EmployeeRepository
import org.jetbrains.exposed.sql.transactions.transaction
import team.mediagroup.models.Employees
import java.time.LocalDate

class EmployeeService(
    private val repository: EmployeeRepository
) {

    // Get employees with optional pagination/filtering
    fun getEmployeesForUser(
        user: UserPrincipal,
        departmentId: Int? = null,
        page: Int = 1,
        size: Int = 50,
        sort: Column<*> = Employees.id
    ): List<EmployeeResponse> = transaction {
        val employees = when (user.role) {
            Role.ADMIN -> repository.findAll(page = page, size = size, sort = sort)
            Role.HR, Role.VIEWER -> {
                val deptId = departmentId ?: user.departmentId ?: return@transaction emptyList()
                repository.findByDepartment(deptId, page = page, size = size, sort = sort)
            }
        }
        employees.map { it.toResponse() }
    }



    // Get single employee
    fun getEmployeeById(id: Int): EmployeeResponse? = transaction {
        val employee = repository.findById(id) ?: return@transaction null
        employee.toResponse()
    }

    fun canView(user: UserPrincipal, employee: EmployeeResponse): Boolean =
        when (user.role) {
            Role.ADMIN -> true
            Role.HR, Role.VIEWER -> user.departmentId == employee.departmentId
        }

    fun canEdit(user: UserPrincipal, employee: EmployeeResponse): Boolean =
        when (user.role) {
            Role.ADMIN -> true
            Role.HR -> user.departmentId == employee.departmentId
            else -> false
        }

    // Full update with logging
    fun updateEmployee(employeeId: Int, request: EmployeeUpdateRequest, principal: UserPrincipal): EmployeeResponse = transaction {
        val employee = repository.findById(employeeId) ?: throw IllegalArgumentException("Employee not found")

        val deptId = request.departmentId ?: employee.department.id.value
        if (repository.existsDuplicate(request.firstName, request.lastName, request.middleName, deptId, employee.id.value))
            throw IllegalArgumentException("Duplicate employee exists in the same department")

        val updated = repository.update(employee) {
            firstName = request.firstName
            lastName = request.lastName
            middleName = request.middleName
            department = Department[deptId]
            roleInCompany = request.roleInCompany
            isOnHoliday = request.isOnHoliday
        }

        // Audit log
        println("User ${principal.id} updated employee ${employee.id.value}")

        updated.toResponse()
    }

    // Partial update (PATCH) with logging
    fun updateEmployeePartial(employeeId: Int, request: EmployeeUpdateRequest, principal: UserPrincipal): EmployeeResponse = transaction {
        val employee = repository.findById(employeeId) ?: throw IllegalArgumentException("Employee not found")

        val deptId = request.departmentId ?: employee.department.id.value

        if (repository.existsDuplicate(
                request.firstName.takeIf { it.isNotBlank() } ?: employee.firstName,
                request.lastName.takeIf { it.isNotBlank() } ?: employee.lastName,
                request.middleName ?: employee.middleName,
                deptId,
                employee.id.value
            )
        ) throw IllegalArgumentException("Duplicate employee exists in the same department")

        val updated = repository.update(employee) {
            firstName = request.firstName.takeIf { it.isNotBlank() } ?: firstName
            lastName = request.lastName.takeIf { it.isNotBlank() } ?: lastName
            middleName = request.middleName ?: middleName
            department = Department[deptId]
            roleInCompany = request.roleInCompany.takeIf { it.isNotBlank() } ?: roleInCompany
            isOnHoliday = request.isOnHoliday
        }

        println("User ${principal.id} partially updated employee ${employee.id.value}")

        updated.toResponse()
    }

    // Create employee with validation and audit
    fun createEmployee(request: EmployeeUpdateRequest, principal: UserPrincipal): EmployeeResponse = transaction {
        val deptId = request.departmentId ?: throw IllegalArgumentException("departmentId is required")
        // Check for duplicate first
        if (repository.existsDuplicate(request.firstName, request.lastName, request.middleName, deptId)) {
            throw IllegalArgumentException("Duplicate employee exists in the same department")
        }
        val hiredDate = request.hiredAt ?: LocalDate.now()
        val employee = repository.createEmployee(
            firstName = request.firstName,
            lastName = request.lastName,
            middleName = request.middleName,
            departmentId = deptId,
            roleInCompany = request.roleInCompany,
            hiredAt = hiredDate,
            isOnHoliday = request.isOnHoliday
        )

        println("User ${principal.id} created employee ${employee.id.value}")

        employee.toResponse()
    }

    // Delete employee with audit
    fun deleteEmployee(employeeId: Int, principal: UserPrincipal) = transaction {
        repository.delete(employeeId)
        println("User ${principal.id} deleted employee $employeeId")
    }

    // Validation helper
    fun validateEmployeeRequest(request: EmployeeUpdateRequest): List<String> {
        val errors = mutableListOf<String>()
        if (request.firstName.isBlank()) errors.add("firstName is required")
        if (request.lastName.isBlank()) errors.add("lastName is required")
        if (request.roleInCompany.isBlank()) errors.add("roleInCompany is required")
        return errors
    }
}
