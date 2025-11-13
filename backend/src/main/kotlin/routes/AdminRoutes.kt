package team.mediagroup.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import team.mediagroup.dto.UserCreateRequest
import team.mediagroup.dto.UserUpdateRequest
import team.mediagroup.models.*
import team.mediagroup.services.AdminService

fun Route.adminRoutes(adminService: AdminService) {
    authenticate("auth-jwt") {
        route("/api/admin") {
            get("/users") {
                val principal = call.principal<UserPrincipal>() ?: return@get call.respond(HttpStatusCode.Unauthorized)
                if (principal.role != Role.ADMIN) return@get call.respondText("Forbidden", status = io.ktor.http.HttpStatusCode.Forbidden)

                // Extract filters from query params
                val role = call.request.queryParameters["role"]
                val departmentId = call.request.queryParameters["departmentId"]?.toIntOrNull()
                val username = call.request.queryParameters["username"]

                val users = adminService.getAllUsers(role, departmentId, username)
                call.respond(users)
            }

            get("/users/{id}") {
                val principal = call.principal<UserPrincipal>() ?:
                return@get call.respond(HttpStatusCode.Unauthorized)

                if (principal.role != Role.ADMIN)
                    return@get call.respond(HttpStatusCode.Forbidden)

                val id = call.parameters["id"]?.toIntOrNull() ?:
                return@get call.respond(HttpStatusCode.BadRequest, "Invalid user ID")

                val user = adminService.getUserById(id)
                    ?: return@get call.respond(HttpStatusCode.NotFound, "User not found")

                call.respond(user)
            }

            post("/users") {
                val principal = call.principal<UserPrincipal>()!!
                if (principal.role != Role.ADMIN) return@post call.respondText("Forbidden", status = io.ktor.http.HttpStatusCode.Forbidden)

                val request = call.receive<UserCreateRequest>()
                adminService.createUser(request)
                call.respondText("User created")
            }
            put("/users/{id}") {
                val principal = call.principal<UserPrincipal>() ?: return@put call.respond(HttpStatusCode.Forbidden)
                if (principal.role != Role.ADMIN) return@put call.respond(HttpStatusCode.Forbidden)

                val id = call.parameters["id"]?.toIntOrNull() ?: return@put call.respond(HttpStatusCode.BadRequest)
                val request = call.receive<UserUpdateRequest>()
                adminService.updateUser(id, request)
                call.respond(mapOf("message" to "User updated"))
            }

            delete("/users/{id}") {
                val principal = call.principal<UserPrincipal>() ?: return@delete call.respond(HttpStatusCode.Forbidden)
                if (principal.role != Role.ADMIN) return@delete call.respond(HttpStatusCode.Forbidden)

                val id = call.parameters["id"]?.toIntOrNull() ?: return@delete call.respond(HttpStatusCode.BadRequest)
                adminService.deleteUser(id)
                call.respond(mapOf("message" to "User deleted"))
            }

        }
    }
}
