package team.mediagroup.routes

import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.transactions.transaction
import team.mediagroup.dto.EmployeeUpdateRequest
import team.mediagroup.dto.EmployeeResponse
import team.mediagroup.models.*
import team.mediagroup.services.EmployeeService

fun Route.employeeRoutes(employeeService: EmployeeService) {
    authenticate("auth-jwt") {
        route("/api/employees") {

            // Get all employees (filtered by role)
            get {
                val principal = call.principal<UserPrincipal>()!!
                val employees = employeeService.getEmployeesForUser(principal)
                call.respond(employees) // already EmployeeResponse
            }

            // Get a single employee
            get("/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = io.ktor.http.HttpStatusCode.BadRequest)

                val employee = employeeService.getEmployeeById(id)
                    ?: return@get call.respondText("Employee not found", status = io.ktor.http.HttpStatusCode.NotFound)

                if (!employeeService.canView(principal, employee)) {
                    return@get call.respondText("Not allowed", status = io.ktor.http.HttpStatusCode.Forbidden)
                }

                // convert to DTO for safe serialization
                val dto = EmployeeResponse(
                    id = employee.id.value,
                    firstName = employee.firstName,
                    lastName = employee.lastName,
                    middleName = employee.middleName,
                    departmentId = employee.departmentId.value,
                    roleInCompany = employee.roleInCompany,
                    hiredAt = employee.hiredAt.toString(),
                    isOnHoliday = employee.isOnHoliday
                )

                call.respond(dto)
            }

            // Create a new employee
            post {
                val principal = call.principal<UserPrincipal>()!!
                if (principal.role != Role.ADMIN && principal.role != Role.HR) {
                    return@post call.respondText("Not allowed", status = io.ktor.http.HttpStatusCode.Forbidden)
                }

                val req = call.receive<EmployeeUpdateRequest>()

                // departmentId is required for creating an employee in this schema
                val deptId = req.departmentId ?: return@post call.respondText(
                    "departmentId is required",
                    status = io.ktor.http.HttpStatusCode.BadRequest
                )

                // ensure department exists
                val dept = transaction { Department.findById(deptId) }
                    ?: return@post call.respondText("Department not found", status = io.ktor.http.HttpStatusCode.BadRequest)

                val newEmployee: Employee = transaction {
                    Employee.new {
                        firstName = req.firstName
                        lastName = req.lastName
                        middleName = req.middleName
                        departmentId = EntityID(deptId, Departments)
                        roleInCompany = req.roleInCompany
                        hiredAt = req.hiredAt ?: java.time.LocalDate.now()
                        isOnHoliday = req.isOnHoliday
                    }
                }

                val dto = EmployeeResponse(
                    id = newEmployee.id.value,
                    firstName = newEmployee.firstName,
                    lastName = newEmployee.lastName,
                    middleName = newEmployee.middleName,
                    departmentId = newEmployee.departmentId.value,
                    roleInCompany = newEmployee.roleInCompany,
                    hiredAt = newEmployee.hiredAt.toString(),
                    isOnHoliday = newEmployee.isOnHoliday
                )

                call.respond(dto)
            }

            // Update an existing employee
            post("/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@post call.respondText("Invalid ID", status = io.ktor.http.HttpStatusCode.BadRequest)

                val employee = employeeService.getEmployeeById(id)
                    ?: return@post call.respondText("Employee not found", status = io.ktor.http.HttpStatusCode.NotFound)

                if (!employeeService.canEdit(principal, employee)) {
                    return@post call.respondText("Not allowed", status = io.ktor.http.HttpStatusCode.Forbidden)
                }

                val updateRequest = call.receive<EmployeeUpdateRequest>()
                employeeService.updateEmployee(employee, updateRequest)

                // return updated DTO
                val updatedDto = EmployeeResponse(
                    id = employee.id.value,
                    firstName = employee.firstName,
                    lastName = employee.lastName,
                    middleName = employee.middleName,
                    departmentId = employee.departmentId.value,
                    roleInCompany = employee.roleInCompany,
                    hiredAt = employee.hiredAt.toString(),
                    isOnHoliday = employee.isOnHoliday
                )
                call.respond(updatedDto)
            }

            // Delete an employee
            delete("/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = io.ktor.http.HttpStatusCode.BadRequest)

                val employee = employeeService.getEmployeeById(id)
                    ?: return@delete call.respondText("Employee not found", status = io.ktor.http.HttpStatusCode.NotFound)

                if (principal.role != Role.ADMIN && principal.role != Role.HR) {
                    return@delete call.respondText("Not allowed", status = io.ktor.http.HttpStatusCode.Forbidden)
                }

                transaction { employee.delete() }
                call.respondText("Deleted successfully")
            }
        }
    }
}
