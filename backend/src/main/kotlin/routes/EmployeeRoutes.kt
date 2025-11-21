package team.mediagroup.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SortOrder
import team.mediagroup.dto.EmployeeUpdateRequest
import team.mediagroup.models.Role
import team.mediagroup.models.UserPrincipal
import team.mediagroup.services.EmployeeService
import team.mediagroup.mappers.toResponse
import team.mediagroup.models.Employees

fun Route.employeeRoutes(employeeService: EmployeeService) {
    authenticate("auth-jwt") {
        route("/api/employees") {

            // GET /api/employees?departmentId=&page=&size=&sort=
            get {
                val principal = call.principal<UserPrincipal>()!!

                val departmentId = call.request.queryParameters["departmentId"]?.toIntOrNull()
                val page = call.request.queryParameters["page"]?.toIntOrNull() ?: 1
                val size = call.request.queryParameters["size"]?.toIntOrNull() ?: 50
                val sortParam = call.request.queryParameters["sort"] ?: "id,asc"

                // Split sortParam into field and direction
                val (sortField, sortDir) = sortParam.split(",").let {
                    it[0].lowercase() to (it.getOrNull(1)?.lowercase() ?: "asc")
                }

                // Map string to actual Exposed Column
                val sortColumn: Column<*> = when (sortField) {
                    "firstname" -> Employees.firstName
                    "lastname" -> Employees.lastName
                    "hiredat" -> Employees.hiredAt
                    "departmentid" -> Employees.departmentId
                    "roleincompany" -> Employees.roleInCompany
                    else -> Employees.id
                }

                // Determine sort order
                val order = if (sortDir == "desc") SortOrder.DESC else SortOrder.ASC

                // Fetch employees with pagination and sorting
                val employees = employeeService.getEmployeesForUser(principal, departmentId, page, size, sortColumn, order)

                call.respond(HttpStatusCode.OK, employees)
            }


            // GET /api/employees/{id}
            get("/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@get call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val employee = employeeService.getEmployeeById(id)
                    ?: return@get call.respondText("Employee not found", status = HttpStatusCode.NotFound)

                if (!employeeService.canView(principal, employee)) {
                    return@get call.respondText("Not allowed", status = HttpStatusCode.Forbidden)
                }

                call.respond(HttpStatusCode.OK, employee)
            }

            // POST /api/employees
            post {
                val principal = call.principal<UserPrincipal>()!!
                if (principal.role !in listOf(Role.ADMIN, Role.HR)) {
                    return@post call.respondText("Not allowed", status = HttpStatusCode.Forbidden)
                }

                val request = call.receive<EmployeeUpdateRequest>()

                // Validation
                val errors = employeeService.validateEmployeeRequest(request)
                if (errors.isNotEmpty()) {
                    return@post call.respond(HttpStatusCode.BadRequest, mapOf("errors" to errors))
                }

                try {
                    val newEmployee = employeeService.createEmployee(request, principal)
                    call.respond(HttpStatusCode.Created, newEmployee)
                } catch (e: IllegalArgumentException) {
                    // Here duplicate or missing department errors will be caught
                    call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
                } catch (e: Exception) {
                    call.application.environment.log.error("Failed to create employee", e)
                    call.respond(HttpStatusCode.InternalServerError, "Failed to create employee")
                }
            }

            // PUT /api/employees/{id}
            put("/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<EmployeeUpdateRequest>()

                val errors = employeeService.validateEmployeeRequest(request)
                if (errors.isNotEmpty()) {
                    return@put call.respond(HttpStatusCode.BadRequest, mapOf("errors" to errors))
                }

                try {
                    val employeeBeforeUpdate = employeeService.getEmployeeById(id)
                        ?: return@put call.respondText("Employee not found", status = HttpStatusCode.NotFound)

                    if (!employeeService.canEdit(principal, employeeBeforeUpdate)) {
                        return@put call.respondText("Not allowed", status = HttpStatusCode.Forbidden)
                    }

                    val updatedEmployee = employeeService.updateEmployee(id, request, principal)
                    call.respond(HttpStatusCode.OK, updatedEmployee)
                } catch (e: Exception) {
                    call.application.environment.log.error("Failed to update employee", e)
                    call.respond(HttpStatusCode.InternalServerError, "Failed to update employee")
                }
            }

            // PATCH /api/employees/{id} → partial update
            patch("/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@patch call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val request = call.receive<EmployeeUpdateRequest>()
                try {
                    val updatedEmployee = employeeService.updateEmployeePartial(id, request, principal)
                    call.respond(HttpStatusCode.OK, updatedEmployee)
                } catch (e: Exception) {
                    call.application.environment.log.error("Failed to partially update employee", e)
                    call.respond(HttpStatusCode.InternalServerError, "Failed to update employee")
                }
            }

            // DELETE /api/employees/{id}
            delete("/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                try {
                    val employee = employeeService.getEmployeeById(id)
                        ?: return@delete call.respondText("Employee not found", status = HttpStatusCode.NotFound)

                    if (principal.role !in listOf(Role.ADMIN, Role.HR)) {
                        return@delete call.respondText("Not allowed", status = HttpStatusCode.Forbidden)
                    }

                    employeeService.deleteEmployee(id, principal)
                    call.respond(HttpStatusCode.OK, "Deleted successfully")
                } catch (e: Exception) {
                    call.application.environment.log.error("Failed to delete employee", e)
                    call.respond(HttpStatusCode.InternalServerError, "Failed to delete employee")
                }
            }
        }
    }
}
