package team.mediagroup

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import team.mediagroup.models.Departments
import team.mediagroup.models.Employees
import team.mediagroup.models.HolidayHistories
import team.mediagroup.models.Role
import team.mediagroup.models.UserPrincipal
import team.mediagroup.models.Users
import team.mediagroup.services.AdminService
import team.mediagroup.services.AuthService
import team.mediagroup.services.EmployeeService

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    // Initialize database connection
    Database.connect(
        url = "jdbc:postgresql://localhost:5432/evms_db",
        driver = "org.postgresql.Driver",
        user = "postgres",
        password = "1234"
    )

    transaction {
        println("Creating tables...")
        SchemaUtils.create(
            Departments,
            Users,
            Employees,
            HolidayHistories
        )
    }
    install(io.ktor.server.plugins.statuspages.StatusPages) {
        exception<Throwable> { call, cause ->
            cause.printStackTrace() // print in console
            call.respondText("Server error: ${cause.message}", status = io.ktor.http.HttpStatusCode.InternalServerError)
        }
    }
    val jwtSecret = "super-secret-key"
    val authService = AuthService(jwtSecret)
    val adminService = AdminService(authService)
    val employeeService = EmployeeService()

    install(Authentication) {
        jwt("auth-jwt") {
            realm = "evms-app"
            verifier(authService.getVerifier())
            validate { credential ->
                val userId = credential.payload.getClaim("userId").asInt()
                val roleStr = credential.payload.getClaim("role").asString()
                if (userId != null && roleStr != null) {
                    val deptId = transaction {
                        Users.select { Users.id eq userId }
                            .singleOrNull()?.get(Users.departmentId)
                    }
                    UserPrincipal(id = userId, role = Role.valueOf(roleStr), departmentId = deptId)
                } else null
            }
        }
    }

    // ✅ Load all routes (from Routing.kt)
    configureRouting(authService, adminService, employeeService)
}
