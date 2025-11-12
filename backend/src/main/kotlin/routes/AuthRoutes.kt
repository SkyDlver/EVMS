package team.mediagroup.routes

import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import team.mediagroup.models.Users
import team.mediagroup.services.AuthService

@Serializable
data class LoginRequest(val username: String, val password: String)

fun Route.authRoutes(authService: AuthService){
    route("/api") {
        post("/login") {
            val login = call.receive<LoginRequest>()
            val user = transaction {
                Users.select { Users.username eq login.username }.singleOrNull()
            }

            if (user != null && authService.verifyPassword(login.password, user[Users.passwordHash])) {
                val token = authService.generateToken(user[Users.id].value, user[Users.role].name)
                call.respond(mapOf("token" to token))
            } else {
                call.respondText("Invalid credentials", status = io.ktor.http.HttpStatusCode.Unauthorized)
            }
        }
    }
}
