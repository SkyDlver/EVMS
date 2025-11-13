package team.mediagroup.routes

import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team.mediagroup.dto.EmployeeUpdateRequest
import team.mediagroup.models.Role
import team.mediagroup.models.UserPrincipal
import team.mediagroup.services.EmployeeService
import team.mediagroup.mappers.toResponse

fun Route.employeeRoutes(employeeService: EmployeeService) {
    authenticate("auth-jwt") {
        route("/api/employees") {

            // GET /api/employees
            get {
                val principal = call.principal<UserPrincipal>()!!
                val employees = employeeService.getEmployeesForUser(principal)
                call.respond(employees)
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

                call.respond(employee.toResponse())
            }

            // POST /api/employees  → create new employee
            post {
                val principal = call.principal<UserPrincipal>()!!
                if (principal.role != Role.ADMIN && principal.role != Role.HR) {
                    return@post call.respondText("Not allowed", status = HttpStatusCode.Forbidden)
                }

                val request = call.receive<EmployeeUpdateRequest>()

                try {
                    val newEmployee = employeeService.createEmployee(request)
                    call.respond(HttpStatusCode.Created, newEmployee)
                } catch (e: IllegalArgumentException) {
                    call.respondText(e.message ?: "Invalid data", status = HttpStatusCode.BadRequest)
                }
            }

            // PUT /api/employees/{id}  → update employee (REST-compliant)
            put("/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@put call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val employee = employeeService.getEmployeeById(id)
                    ?: return@put call.respondText("Employee not found", status = HttpStatusCode.NotFound)

                if (!employeeService.canEdit(principal, employee)) {
                    return@put call.respondText("Not allowed", status = HttpStatusCode.Forbidden)
                }

                val updateRequest = call.receive<EmployeeUpdateRequest>()

                try {
                    val updated = employeeService.updateEmployee(employee, updateRequest)
                    call.respond(HttpStatusCode.OK, updated)
                } catch (e: IllegalArgumentException) {
                    call.respondText(e.message ?: "Invalid data", status = HttpStatusCode.BadRequest)
                }
            }


            // DELETE /api/employees/{id}
            delete("/{id}") {
                val principal = call.principal<UserPrincipal>()!!
                val id = call.parameters["id"]?.toIntOrNull()
                    ?: return@delete call.respondText("Invalid ID", status = HttpStatusCode.BadRequest)

                val employee = employeeService.getEmployeeById(id)
                    ?: return@delete call.respondText("Employee not found", status = HttpStatusCode.NotFound)

                if (principal.role != Role.ADMIN && principal.role != Role.HR) {
                    return@delete call.respondText("Not allowed", status = HttpStatusCode.Forbidden)
                }

                employeeService.deleteEmployee(id)
                call.respondText("Deleted successfully", status = HttpStatusCode.OK)
            }
        }
    }
}
