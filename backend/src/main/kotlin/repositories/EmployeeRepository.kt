package team.mediagroup.repositories

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.transactions.transaction
import team.mediagroup.models.*

class EmployeeRepository {

    fun findAll(
        page: Int = 1,
        size: Int = 50,
        sort: Column<*> = Employees.id,
        order: SortOrder = SortOrder.ASC
    ): List<Employee> = transaction {
        Employee.all()
            .orderBy(sort to order)
            .limit(size, offset = ((page - 1) * size).toLong())
            .toList()
    }

    fun findByDepartment(
        departmentId: Int,
        page: Int = 1,
        size: Int = 50,
        sort: Column<*> = Employees.id,
        order: SortOrder = SortOrder.ASC
    ): List<Employee> = transaction {
        Employee.find { Employees.departmentId eq departmentId }
            .orderBy(sort to order)
            .limit(size, offset = ((page - 1) * size).toLong())
            .toList()
    }

    fun findById(id: Int): Employee? = transaction {
        Employee.findById(id)
    }

    fun existsDuplicate(
        firstName: String,
        lastName: String,
        middleName: String? = null,
        departmentId: Int,
        excludeId: Int? = null
    ): Boolean = transaction {
        val baseCondition = (Employees.firstName eq firstName) and
                (Employees.lastName eq lastName) and
                (Employees.departmentId eq EntityID(departmentId, Departments))

        val middleCondition = if (middleName.isNullOrBlank())
            (Employees.middleName.isNull() or (Employees.middleName eq ""))
        else
            (Employees.middleName eq middleName)

        val fullCondition = if (excludeId != null)
            baseCondition and middleCondition and (Employees.id neq excludeId)
        else
            baseCondition and middleCondition

        Employees.select { fullCondition }.limit(1).any()
    }

    fun createEmployee(
        firstName: String,
        lastName: String,
        middleName: String? = null,
        departmentId: Int,
        roleInCompany: String,
        hiredAt: java.time.LocalDate,
        isOnHoliday: Boolean
    ): Employee = transaction {
        Employee.new {
            this.firstName = firstName
            this.lastName = lastName
            this.middleName = middleName
            department = Department[departmentId]
            this.roleInCompany = roleInCompany
            this.hiredAt = hiredAt
            this.isOnHoliday = isOnHoliday
        }
    }

    fun update(employee: Employee, block: Employee.() -> Unit): Employee = transaction {
        employee.apply(block)
    }

    fun delete(employeeId: Int) = transaction {
        Employee.findById(employeeId)?.delete()
    }
}
