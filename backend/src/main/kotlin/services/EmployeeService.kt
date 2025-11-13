package team.mediagroup.services

import team.mediagroup.dto.EmployeeResponse
import team.mediagroup.dto.EmployeeUpdateRequest
import team.mediagroup.mappers.toResponse
import team.mediagroup.models.Department
import team.mediagroup.models.Employee
import team.mediagroup.models.Role
import team.mediagroup.models.UserPrincipal
import team.mediagroup.repositories.EmployeeRepository
import java.time.LocalDate

class EmployeeService(
    private val repository: EmployeeRepository
) {

    fun getEmployeesForUser(user: UserPrincipal): List<EmployeeResponse> {
        val employees = when (user.role) {
            Role.ADMIN -> repository.findAll()
            Role.HR, Role.VIEWER -> {
                val deptId = user.departmentId ?: return emptyList()
                repository.findByDepartment(deptId)
            }
        }
        return employees.map { it.toResponse() }
    }

    fun getEmployeeById(id: Int): Employee? {
        return repository.findById(id)
    }

    fun canView(user: UserPrincipal, employee: Employee): Boolean = when (user.role) {
        Role.ADMIN -> true
        Role.HR, Role.VIEWER -> user.departmentId == employee.department.id.value
    }

    fun canEdit(user: UserPrincipal, employee: Employee): Boolean = when (user.role) {
        Role.ADMIN -> true
        Role.HR -> user.departmentId == employee.department.id.value
        else -> false
    }

    fun updateEmployee(employee: Employee, updateRequest: EmployeeUpdateRequest): EmployeeResponse {
        val deptId = updateRequest.departmentId ?: employee.department.id.value

        if (repository.existsDuplicate(
                updateRequest.firstName,
                updateRequest.lastName,
                updateRequest.middleName,
                deptId,
                employee.id.value
            )
        ) throw IllegalArgumentException("Duplicate employee exists in the same department")

        val updated = repository.update(employee) {
            firstName = updateRequest.firstName
            lastName = updateRequest.lastName
            middleName = updateRequest.middleName
            department = Department[deptId]
            roleInCompany = updateRequest.roleInCompany
            isOnHoliday = updateRequest.isOnHoliday
        }

        return updated.toResponse()
    }

    fun createEmployee(request: EmployeeUpdateRequest): EmployeeResponse {
        val deptId = request.departmentId ?: throw IllegalArgumentException("departmentId is required")

        if (repository.existsDuplicate(request.firstName, request.lastName, request.middleName, deptId))
            throw IllegalArgumentException("Duplicate employee exists in the same department")

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

        return employee.toResponse()
    }

    fun deleteEmployee(employeeId: Int) {
        repository.delete(employeeId)
    }
}
